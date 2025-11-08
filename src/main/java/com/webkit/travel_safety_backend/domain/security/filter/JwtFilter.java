package com.webkit.travel_safety_backend.domain.security.filter;

import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.repository.RefreshTokenRepository;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.resolverToken(request);

        try {
            if (accessToken != null) {
                Integer status = jwtProvider.validateAccessToken(accessToken);

                if (Objects.equals(status, AuthenticationStatCode.VALID_STATE)) {
                    saveAuthentication(accessToken);
                }

                if (Objects.equals(status, AuthenticationStatCode.EXPIRED_STATE)) {
                    Long userId = jwtProvider.getUserId(accessToken);
                    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.getRefreshTokenByUserId(userId)
                            .orElseThrow(() -> new RuntimeException("Refresh token not found"));
                    String clientRefreshToken = this.getRefreshTokenFromCookie(request);

                    Integer refreshTokenValidStatus = jwtProvider.validateRefreshToken(refreshTokenEntity, clientRefreshToken);
                    if (!refreshTokenValidStatus.equals(AuthenticationStatCode.VALID_STATE)) {
                        throw new RuntimeException("Invalid refresh token");
                    }

                    Users user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    // 새 AccessToken 발급
                    String newAccessToken = jwtProvider.generateAccessToken(userId, user.getRole());
                    saveAuthentication(newAccessToken);

                    response.addHeader("Authorization", "Bearer " + newAccessToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }

    }

    private void saveAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "refresh_token");
        return cookie != null ? cookie.getValue() : null;
    }
}
