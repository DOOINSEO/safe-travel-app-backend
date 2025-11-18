package com.webkit.travel_safety_backend.domain.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.mapper.UserMapper;
import com.webkit.travel_safety_backend.domain.security.filter.*;
import com.webkit.travel_safety_backend.domain.security.utils.JwtProvider;
import com.webkit.travel_safety_backend.domain.security.utils.JwtService;
import com.webkit.travel_safety_backend.domain.security.utils.ValidStatusCode;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] LOGIN_URL = {"/api/user", "/api/user/login"};
    private final String[] AI_URL = {"/api/news/**", "/api/risk/**"};

    @Bean JwtFilter jwtFilter(JwtProvider jwtProvider, JwtService jwtService) {
        return new JwtFilter(jwtProvider, jwtService);
    }

//    @Bean LoginFilter loginFilter(AuthenticationConfiguration configuration, JwtProvider jwtProvider, JwtService jwtService) throws Exception {
//        LoginFilter loginFilter = new LoginFilter(configuration.getAuthenticationManager(), jwtProvider, jwtService);
//        loginFilter.setFilterProcessesUrl("/api/user/login");
//        return loginFilter;
//    }


    // SecurityFilter 자체가 Session 기반으로 설계된 듯.
    // 내부 구조상 LogoutFilter가 JwtFiler, LoginFilter보다 앞에 있음.
    // 나중에 Login, Logout Controller를 만드는게 구조상 좋을 듯.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AuthenticationConfiguration configuration,
            JwtFilter jwtFilter,
            JwtProvider jwtProvider,
            JwtService jwtService,
            UserMapper userMapper
    ) throws Exception {
        LoginFilter loginFilter = new LoginFilter(configuration.getAuthenticationManager(),userMapper, jwtProvider, jwtService);
        loginFilter.setFilterProcessesUrl("/api/user/login");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, AI_URL).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/user/logout")
                        .deleteCookies("refresh_token")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            logoutProcessHandler(jwtProvider, jwtService, request, response);

                            //TODO 1. 회원 정보 삭제할 경우, logout처리
                        })
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("http://localhost:5713"));
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET",  "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void logoutProcessHandler(JwtProvider jwtProvider, JwtService jwtService, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accessToken = jwtProvider.resolverAccessToken(request);
            if (accessToken == null) throw new RuntimeException("Invalid access token");

            Long userId = jwtProvider.getUserId(accessToken);
            RefreshTokenEntity refreshTokenEntity = jwtService.getRefreshTokenEntity(userId);
            log.info("Refresh Token = {} ", refreshTokenEntity.getRefreshToken());
            Integer validStatus = jwtProvider.validateAccessToken(accessToken, refreshTokenEntity);
            if (!Objects.equals(validStatus, ValidStatusCode.VALID_STATE)) {
                throw new RuntimeException("Invalid access token");
            }

            jwtService.deleteRefreshToken(userId);

            ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
                    .path("/")
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.success("Logout Success"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.fail(e.getMessage()));
        }
    }
}
