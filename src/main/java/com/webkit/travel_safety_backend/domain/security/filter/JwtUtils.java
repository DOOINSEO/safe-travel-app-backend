//package com.webkit.travel_safety_backend.domain.security.filter;
//
//import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
//import com.webkit.travel_safety_backend.domain.model.entity.Users;
//import com.webkit.travel_safety_backend.domain.repository.RefreshTokenRepository;
//import com.webkit.travel_safety_backend.domain.repository.UserRepository;
//import io.jsonwebtoken.Jwts;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Date;
//
//@Slf4j
//@Component
//public class JwtUtils {
//
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserRepository userRepository;
//    private final SecretKey secretKey;
//
//    public JwtUtils(@Value("${spring.jwt.secret}") String secret,  RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
//        secretKey = new SecretKeySpec(
//                secret.getBytes(StandardCharsets.UTF_8),
//                //HMAC SHA256 알고리즘
//                Jwts.SIG.HS256.key().build().getAlgorithm()
//        );
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.userRepository = userRepository;
//    }
//
//    public Long getId(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .get("id", Long.class);
//    }
//
//    public String getRole(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .get("role", String.class);
//    }
//
//    public Date getExpiration(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getExpiration();
//    }
//
//    public Boolean isExpired(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getExpiration()
//                .before(new Date());
//    }
//
//    public String createJwt(Long id, String role, Long expiredMs) {
//        return Jwts.builder()
//                .claim("id", id)
//                .claim("role", role)
//                .issuedAt(new Date(System.currentTimeMillis())) // 발행일
//                .expiration(new Date(System.currentTimeMillis() + expiredMs))
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public RefreshTokenEntity saveToken(String token, Users user) {
//        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
//        refreshTokenEntity.setToken(token);
//        refreshTokenEntity.setExpiration(this.getExpiration(token));
//
//        RefreshTokenEntity savedToken = refreshTokenRepository.save(refreshTokenEntity);
//        user.setRefreshToken(savedToken);
//        return savedToken;
//    }
//
//    public String getRefreshToken(HttpServletRequest  request) {
//        if (request.getCookies() == null) {
//            return null; // 쿠키가 아예 없는 경우
//        }
//
//        return Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("refresh_token")) // 이름 주의
//                .map(Cookie::getValue) // 실제 토큰 문자열만 추출
//                .findFirst()
//                .orElse(null);
//    }
//
//
//    public void deleteRefreshToken(Long userId) {
//        Users user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
//
//        refreshTokenRepository.delete(user.getRefreshToken());
//    }
//}
