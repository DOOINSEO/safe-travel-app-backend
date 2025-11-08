package com.webkit.travel_safety_backend.domain.service;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.UserMapper;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    public UserResDTO getUserById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.toUserResDTO(users);
    }

    public UserResDTO saveUser(UserReqDTO userReqDTO) {
        userReqDTO.setPassword(bCryptPasswordEncoder.encode(userReqDTO.getPassword()));

        Users user = userRepository.save(userMapper.toUsers(userReqDTO));

        return userMapper.toUserResDTO(user);
    }


    @Transactional
    public UserResDTO updateUser(Long userId, UserReqDTO userReqDTO) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userMapper.updateUserFromDto(userReqDTO, user);
        Users updatedUser = userRepository.save(user);

        return userMapper.toUserResDTO(updatedUser);
    }

    @Transactional
    public UserResDTO deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);

        return userMapper.toUserResDTO(user);
    }

}
