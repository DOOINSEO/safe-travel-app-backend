package com.webkit.travel_safety_backend.domain.security.filter;

import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Role;
import com.webkit.travel_safety_backend.domain.repository.RefreshTokenRepository;
import com.webkit.travel_safety_backend.domain.security.custom.CustomUserDetails;
import com.webkit.travel_safety_backend.domain.security.custom.CustomUserDetailsService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Lob;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

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
        this.secretKey = new SecretKeySpec(
                secretKeyPlain.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    //Jwt accessToken 생성
    public String generateAccessToken(Long userId, Role role) {
        Date now = new Date();
        log.info("expiration ={}", accessExpiration);

        return Jwts.builder()
                .claims()
                .issuer(issuer)
                .issuedAt(now)
                .add("id", userId)
                .add("role", role)
                .expiration(new Date(now.getTime() + accessExpiration))
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

    /* Spring Security에서 UsernamePasswordAuthenticationFilter는 인증 성공하면
     successfulAuthentication()을 호출하며 검증에서 사용한 userDetails 객체를 자동으로 SecurityContext에 저장.
     하지만 직접 상속받아 successfulAuthentication()을 오버라이드한 경우, 직접 수동으로 넣어 줘야 함.
     근데 이거 필요할까? AuthenticationProvider에서 조회한 UserDatils를 Authentication으로 반환하지 않나?
     */
    public Authentication getAuthentication(String accessToken) {
        CustomUserDetails userDetails = userDetailsService.loadUserById(this.getUserId(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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

    // Request Header에서 AccessToken 추출
    public String resolverToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public RefreshTokenEntity saveRefreshToken(Long userId, String refreshToken) {
        Date now = new Date();
        System.out.println(now);
        return refreshTokenRepository.save(new RefreshTokenEntity(
                null,
                refreshToken,
                new Date(now.getTime() + refreshExpiration),
                userId));
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
            refreshTokenRepository.deleteByUserId(userId);
    }


    public Integer validateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken);

            if (claims.getPayload().getExpiration().before(new Date())) {
                return AuthenticationStatCode.EXPIRED_STATE;
            }
            return AuthenticationStatCode.VALID_STATE;

        } catch (SecurityException | MalformedJwtException e) {
            return AuthenticationStatCode.MANIPULATED_STATE;
        } catch (ExpiredJwtException e) {
            return  AuthenticationStatCode.EXPIRED_STATE;
        } catch (UnsupportedJwtException e) {
            return  AuthenticationStatCode.UNSUPPORTED_STATE;
        } catch (IllegalArgumentException e) {
            return  AuthenticationStatCode.WRONG_STATE;
        }
    }

    public Integer validateRefreshToken(RefreshTokenEntity refreshTokenEntity, String clientRefreshToken) {
        if (refreshTokenEntity.getExpiration().before(new Date())) {
            return AuthenticationStatCode.EXPIRED_STATE;
        }
        if (!refreshTokenEntity.getRefreshToken().equals(clientRefreshToken)) {
            return AuthenticationStatCode.MANIPULATED_STATE;
        }

        return AuthenticationStatCode.VALID_STATE;
    }

}
