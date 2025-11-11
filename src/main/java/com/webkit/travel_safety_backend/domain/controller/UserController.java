package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.service.UserService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResDTO>> getUser(@PathVariable Long id) {
        UserResDTO userResDTO = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResDTO>> postUser (@ModelAttribute UserReqDTO userReqDTO) {
        UserResDTO userResDTO = userService.saveUser(userReqDTO);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResDTO>> putUser (@PathVariable Long id,
                                            @ModelAttribute UserReqDTO userReqDTO) {
        UserResDTO userResDTO = userService.updateUser(id, userReqDTO);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResDTO>> deleteUser (@PathVariable Long id) {
        UserResDTO userResDTO = userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(userResDTO));
    }

}
