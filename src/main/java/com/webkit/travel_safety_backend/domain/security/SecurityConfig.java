package com.webkit.travel_safety_backend.domain.security;

import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.security.filter.JwtFilter;
import com.webkit.travel_safety_backend.domain.security.filter.JwtProvider;
import com.webkit.travel_safety_backend.domain.security.filter.LoginFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] WHITELIST = {"/api/user", "/api/user/login"};
    private final JwtProvider jwtProvider;
    private final JwtFilter jwtFilter;


    @Value("${spring.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${spring.jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AuthenticationManager authenticationManager,
            JwtFilter jwtFilter
    ) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtProvider, accessExpiration, refreshExpiration);
        loginFilter.setFilterProcessesUrl("/api/user/login");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/user/logout")
                        .deleteCookies("refresh_token")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            try {
                                String token = jwtProvider.resolverToken(request);
                                if (token != null) {
                                    Long userId = jwtProvider.getUserId(token);
                                    log.info(String.format("User %s has been logged in", userId));
                                    jwtProvider.deleteRefreshToken(userId);
                                }

                                ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
                                        .path("/")
                                        .maxAge(0)
                                        .httpOnly(true)
                                        .secure(true)
                                        .sameSite("None")
                                        .build();

                                response.addHeader("Set-Cookie", expiredCookie.toString());
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"message\": \"Logout success\"}");
                            } catch (Exception e) {
                                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                response.getWriter().write("{\"error\": \"Logout failed\"}");
                            }

                            //TODO 1. 회원 정보 삭제할 경우, logout처리
                            //TODO 2. DB에 AccessToken 저장 고려, 현재 AccessToken 검증 X, 오직 만료시간 확인만 일어남.
                        })
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
