package com.webkit.travel_safety_backend.domain.service;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.UserMapper;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResDTO getUserById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserResDTO(users);
    }

    public UserResDTO saveUser(UserReqDTO userReqDTO) {
        Users user = userRepository.save(userMapper.toUsers(userReqDTO));
        return userMapper.toUserResDTO(user);
    }

    public UserResDTO updateUser(Long userId, UserReqDTO userReqDTO) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // user.setName(userReqDTO.getName());
        // user.setPhone(userReqDTO.getPhone());
        // user.setNickname(userReqDTO.getNickname());
        // user.setAlarmEnabled(userReqDTO.getAlarmEnabled());
        // user.setLoginId(userReqDTO.getLoginId());
        // user.setPwHash(userReqDTO.getPwHash());
        userMapper.updateUserFromDto(userReqDTO, user);
        Users updatedUser = userRepository.save(user);

        return userMapper.toUserResDTO(updatedUser);
    }

    public UserResDTO deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);
        return userMapper.toUserResDTO(user);
    }
}
