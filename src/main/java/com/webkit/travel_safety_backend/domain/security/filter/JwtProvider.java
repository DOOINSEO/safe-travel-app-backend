package com.webkit.travel_safety_backend.domain.security.filter;

import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Role;
import com.webkit.travel_safety_backend.domain.security.custom.CustomUserDetails;
import com.webkit.travel_safety_backend.domain.security.custom.CustomUserDetailsService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


// Token 생성, 추출, 파싱, 검증
@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class JwtProvider {

    private final CustomUserDetailsService userDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKeyPlain;

    private SecretKey secretKey;

    @Value("${spring.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${spring.jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${spring.jwt.issuer}")
    private String issuer;

    // Bean 생성 전 초기화 작업
    @PostConstruct
    public void init() {
        log.info("JwtProvider secretKeyPlain = {}", secretKeyPlain);
        this.secretKey = new SecretKeySpec(
                secretKeyPlain.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    //Jwt accessToken 생성
    public String generateAccessToken(Long userId, Role role) {
        log.info("expiration ={}", accessExpiration);

        return Jwts.builder()
                .claims()
                .issuer(issuer)
                .issuedAt(new Date())
                .add("id", userId)
                .add("role", role)
                .expiration(this.generateTokenExpiration(accessExpiration))
                .and()
                .signWith(secretKey)
                .compact();
    }

    //Jwt refreshToken 생성
    //Jwt로 생성해도 되지만, 새 accessToken 발급할 때 검증용으로만 사용하므로 굳이 사용자 정보를 담을 필요가 없음
    // UUID로 생성
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public Date generateRefreshTokenExpiration() {
        Date now = new Date();
        return new Date(now.getTime() + refreshExpiration);
    }

    public Date generateTokenExpiration(Long expiration) {
        Date now = new Date();
        return new Date(now.getTime() + expiration);
    }

    public RefreshTokenEntity generateRefreshTokenEntity(Long userId, Role role) {
        String accessToken = this.generateAccessToken(userId, role);
        String refreshToken = this.generateRefreshToken();

        return this.generateRefreshTokenEntity(accessToken, refreshToken);
    }

    public RefreshTokenEntity generateRefreshTokenEntity(String accessToken, String refreshToken) {
        Long userId = this.getUserId(accessToken);

        return new RefreshTokenEntity(
                null,
                accessToken,
                refreshToken,
                this.generateRefreshTokenExpiration(),
                userId
        );
    }

    // accessToken을 파싱해서 Users 엔티티를 가진 Authentication 객체 생성
    // UsernamePasswordAuthenticationToken은 Authentication을 상속받음.
    public Authentication getAuthentication(String accessToken) {
        CustomUserDetails userDetails = userDetailsService.loadUserById(this.getUserId(accessToken));
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUser(),
                null,
                userDetails.getAuthorities());
    }

    public Long getUserId(String accessToken) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .get("id", Long.class);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 토큰 만료돼도 claims는 꺼낼 수 있음
            return e.getClaims().get("id", Long.class);
        }
    }

    public Role getRole(String accessToken) {
        try {
            String role = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .get("role", String.class);
            return Role.valueOf(role);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return  Role.valueOf(e.getClaims().get("role", String.class));
        }
    }

    public Date getExpiration(String accessToken) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .getExpiration();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return e.getClaims().get("expiration", Date.class);
        }
    }

    // Request Header에서 AccessToken 추출
    public String resolverAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    
    public String resolverRefreshToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "refresh_token");
        return cookie != null ? cookie.getValue() : null;
    }
    
    //TODO Validation 로직 분리
    public Integer validateAccessToken(String accessToken, RefreshTokenEntity refreshTokenEntity) {
        try {
            // 유효 시간 검증
            Date expiration = this.getExpiration(accessToken);
            if (expiration == null || expiration.before(new Date())) return ValidStatusCode.EXPIRED_STATE;

            Long userId = this.getUserId(accessToken);
            if (userId == null) return ValidStatusCode.MANIPULATED_STATE;

            // 저장된 토큰과 비교
            if (!accessToken.equals(refreshTokenEntity.getAccessToken())) return ValidStatusCode.WRONG_STATE;

            return ValidStatusCode.VALID_STATE;

        } catch (SecurityException | MalformedJwtException e) {
            return ValidStatusCode.MANIPULATED_STATE;
        } catch (ExpiredJwtException e) {
            return  ValidStatusCode.EXPIRED_STATE;
        } catch (UnsupportedJwtException e) {
            return  ValidStatusCode.UNSUPPORTED_STATE;
        } catch (IllegalArgumentException e) {
            return  ValidStatusCode.WRONG_STATE;
        }
    }


    public Integer validateRefreshToken(RefreshTokenEntity refreshTokenEntity, String clientRefreshToken) {
        try {
            if (refreshTokenEntity.getExpiration().before(new Date())) {
                return ValidStatusCode.EXPIRED_STATE;
            }
            if (!refreshTokenEntity.getRefreshToken().equals(clientRefreshToken)) {
                return ValidStatusCode.MANIPULATED_STATE;
            }

            return ValidStatusCode.VALID_STATE;
        } catch (SecurityException | MalformedJwtException e) {
            return ValidStatusCode.MANIPULATED_STATE;
        } catch (ExpiredJwtException e) {
            return  ValidStatusCode.EXPIRED_STATE;
        } catch (UnsupportedJwtException e) {
            return  ValidStatusCode.UNSUPPORTED_STATE;
        } catch (IllegalArgumentException e) {
            return  ValidStatusCode.WRONG_STATE;
        }
    }

}
