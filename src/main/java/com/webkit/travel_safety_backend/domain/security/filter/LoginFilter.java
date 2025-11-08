package com.webkit.travel_safety_backend.domain.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Role;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.security.custom.CustomUserDetails;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/* Custom UsernamePasswordAuthenticationFilter
* 로그인 인증 필터로, 요청으로 들어온 loginId와 password를 통해 token 생성.
* 생성된 token을 AuthenticationManager로 전달.
* 인증 성공한 경우, successfunlAuthentication 핸들러가 동작하며, 이때 Jwt 토큰을 생성한다.
*
* [주의사항]
* LoginFilter는 Bean으로 관리되지 않으므로, Autowired가 아닌 수동으로 생성자를 호출해야 한다.
* */

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final Long accessExpiration;
    private final Long refreshExpiration;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginId, password);

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Users user = customUserDetails.getUser();

        Long id =  user.getId();
        Role role = user.getRole();


        String accessToken = jwtProvider.generateAccessToken(id, role);
        String refreshToken = jwtProvider.generateRefreshToken();

        // DB 내 Users 테이블에 refreshToken 저장, 좋은 방법은 아님
        RefreshTokenEntity savedRefreshTokenEntity = jwtProvider.saveRefreshToken(id, refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", savedRefreshTokenEntity.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 크로스도메인 허용 시 필요
                .path("/")
                .maxAge(refreshExpiration / 1000) // 초 단위
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(ApiResponse.success("Login Success"));

        response.getWriter().write(res);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
