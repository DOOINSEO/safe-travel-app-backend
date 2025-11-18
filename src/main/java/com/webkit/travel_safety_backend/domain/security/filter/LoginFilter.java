package com.webkit.travel_safety_backend.domain.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Role;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.UserMapper;
import com.webkit.travel_safety_backend.domain.security.custom.CustomUserDetails;
import com.webkit.travel_safety_backend.domain.security.utils.JwtProvider;
import com.webkit.travel_safety_backend.domain.security.utils.JwtService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

/* Custom UsernamePasswordAuthenticationFilter
* 로그인 인증 필터로, 요청으로 들어온 loginId와 password를 통해 token 생성.
* 생성된 token을 AuthenticationManager로 전달.
* 인증 성공한 경우, successfunlAuthentication 핸들러가 동작하며, 이때 Jwt 토큰을 생성한다.
*
* [주의사항]
* LoginFilter는 Bean으로 관리되지 않으므로, Autowired가 아닌 수동으로 생성자를 호출해야 한다.
* */

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);

            String loginId = loginData.get("loginId");
            String password = loginData.get("password");


            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginId, password);

            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Users user = customUserDetails.getUser();

        Long id =  user.getId();
        Role role = user.getRole();

        jwtService.deleteRefreshToken(id);
        RefreshTokenEntity refreshTokenEntity = jwtProvider.generateRefreshTokenEntity(id, role);
        RefreshTokenEntity savedRefreshTokenEntity = jwtService.saveRefreshTokenEntity(refreshTokenEntity);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", savedRefreshTokenEntity.getRefreshToken())
                .httpOnly(true)
                .secure(false) //HTTPS 환경 여부
                .sameSite("Lax") // 크로스도메인 허용 시 필요
                .path("/")
                .maxAge(jwtProvider.getRefreshExpiration() / 1000) // 초 단위
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader("Authorization", "Bearer " + savedRefreshTokenEntity.getAccessToken());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UserResDTO userResDTO = userMapper.toUserResDTO(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(ApiResponse.success(userResDTO));

        response.getWriter().write(res);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.error("unsuccessfulAuthentication : {}", failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(),
                ApiResponse.fail(failed.getMessage()));
    }
}
