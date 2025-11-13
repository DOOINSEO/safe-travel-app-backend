package com.webkit.travel_safety_backend.domain.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Role;
import com.webkit.travel_safety_backend.domain.security.utils.JwtProvider;
import com.webkit.travel_safety_backend.domain.security.utils.JwtService;
import com.webkit.travel_safety_backend.domain.security.utils.ValidStatusCode;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtProvider.resolverAccessToken(request);
            log.debug("accessToken: {}", accessToken);

            if (accessToken != null) {
                Long userId = jwtProvider.getUserId(accessToken);
                Role role = jwtProvider.getRole(accessToken);
                RefreshTokenEntity refreshTokenEntity = jwtService.getRefreshTokenEntity(userId);
                RefreshTokenEntity newRefreshTokenEntity = null;

                // accessToken 검증
                Integer accessTokenValidStatus = jwtProvider.validateAccessToken(accessToken, refreshTokenEntity);
                log.debug("accessTokenValidStatus: {}", accessTokenValidStatus);

                if (Objects.equals(accessTokenValidStatus, ValidStatusCode.VALID_STATE)) {
                    //TODO AccessToken 및 RefreshToken 갱신
                    newRefreshTokenEntity = jwtService.updateRefreshToken(
                            userId,
                            jwtProvider.generateAccessToken(userId, role),
                            jwtProvider.generateRefreshTokenExpiration()
                    );
                    log.debug("newRefreshTokenEntity: {}", newRefreshTokenEntity);
                    saveAuthentication(newRefreshTokenEntity.getAccessToken());

                    response.addHeader("Authorization", "Bearer " + newRefreshTokenEntity.getAccessToken());
                } else if (Objects.equals(accessTokenValidStatus, ValidStatusCode.EXPIRED_STATE)) {
                    String clientRefreshToken = jwtProvider.resolverRefreshToken(request);
                    log.debug("clientRefreshToken: {}", clientRefreshToken);

                    Integer refreshTokenValidStatus = jwtProvider.validateRefreshToken(refreshTokenEntity, clientRefreshToken);
                    log.debug("refreshTokenValidStatus: {}", refreshTokenValidStatus);
                    if (!refreshTokenValidStatus.equals(ValidStatusCode.VALID_STATE)) {
                        jwtService.deleteRefreshToken(userId);
                        throw new RuntimeException("refresh token is invalid. need to login again");
                    }

                    // 새 AccessToken 발급, 일단 refreshToken은 고정
                    String newAccessToken = jwtProvider.generateAccessToken(userId, role);
                    log.debug("newAccessToken: {}", newAccessToken);
                    jwtService.updateAccessToken(userId, newAccessToken);
                    saveAuthentication(newAccessToken);

                    response.addHeader("Authorization", "Bearer " + newAccessToken);
                } else {
                    throw new RuntimeException("Invalid access token");
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JwtFilter exception", e);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getOutputStream(),
                    ApiResponse.fail(e.getMessage()));
        }
    }

    private void saveAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
