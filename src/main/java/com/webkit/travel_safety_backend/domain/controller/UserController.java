package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.service.UserService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResDTO>> getUser(@AuthenticationPrincipal Users user) {
        Long userId =  user.getId();
        log.info("REST request to get User : {}", userId);
        UserResDTO userResDTO = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResDTO>> postUser (@RequestBody UserReqDTO userReqDTO) {
        log.info("REST request to post User : {}", userReqDTO);
        UserResDTO userResDTO = userService.saveUser(userReqDTO);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResDTO>> putUser (@AuthenticationPrincipal Users user,
                                                            @RequestBody UserReqDTO userReqDTO) {
        log.info("REST request to put User : {}", userReqDTO);
        UserResDTO userResDTO = userService.updateUser(user, userReqDTO);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<UserResDTO>> deleteUser (@AuthenticationPrincipal Users user) {
        log.info("REST request to delete User : {}", user);
        UserResDTO userResDTO = userService.deleteUser(user);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

}
