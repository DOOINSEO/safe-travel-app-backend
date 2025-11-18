package com.webkit.travel_safety_backend.domain.service;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.UserMapper;
import com.webkit.travel_safety_backend.domain.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    // TODO 리팩토링
    public UserResDTO getUserById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.toUserResDTO(users);
    }

    public UserResDTO saveUser(UserReqDTO userReqDTO) {
        userReqDTO.setPassword(bCryptPasswordEncoder.encode(userReqDTO.getPassword()));

        Users user = userRepository.save(userMapper.toUsers(userReqDTO));

        log.info("User save successful");
        return userMapper.toUserResDTO(user);
    }


    @Transactional
    public UserResDTO updateUser(Users user, UserReqDTO userReqDTO) {
        log.info("User infomation before update = {}", user);

        userMapper.updateUserFromDto(userReqDTO, user);
        Users updatedUser = userRepository.save(user);

        log.info("updated user = {}", updatedUser);
        return userMapper.toUserResDTO(updatedUser);
    }

    @Transactional
    public UserResDTO deleteUser(Users user) {

        userRepository.delete(user);
        refreshTokenRepository.deleteByUserId(user.getId());

        log.info("User delete successful");
        return userMapper.toUserResDTO(user);
    }

}
