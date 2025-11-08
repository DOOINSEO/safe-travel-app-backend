package com.webkit.travel_safety_backend.domain.security.custom;

import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/* 인증 처리 Service
* Http 요청으로 들어온 loginId와 password로 UsernamePasswordAuthenticationFilter에서 UsernamePasswordAuthenticationToken을 생성함.
* UsernamePasswordAuthenticationToken을 AuthenticationManger로 전달됨.
* AuthenticationManger에서 AuthenticationProvider로 전달됨.
* AuthenticationProvider에서는 UserDetailsService를 찾고 호출하여 UsernamePasswordAuthenticationToken을 검증함.
* 이때 토큰 내 loginId로 DB 내에 Entity를 찾는다. (findById)
* */


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        
        // Exception 대신 null 처리
        Users user = userRepository.findByLoginId(loginId)
                .orElse(null);

        return new CustomUserDetails(user);
    }

    public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Users user = userRepository.findById(id)
                .orElse(null);

        return new CustomUserDetails(user);
    }




}
