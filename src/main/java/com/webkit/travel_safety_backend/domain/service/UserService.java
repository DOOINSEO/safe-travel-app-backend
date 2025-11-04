package com.webkit.travel_safety_backend.domain.service;

import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.UserMapper;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResDTO getUserById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return

    }
}
