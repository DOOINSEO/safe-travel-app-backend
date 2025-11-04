package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ApiResponse<Users> getUser(@PathVariable Long id) {

    }

    @PostMapping
    public ApiResponse<Users> postUser (@ModelAttribute UserReqDTO user) {

    }

    @PutMapping
    public ApiResponse<Users> putUser (@ModelAttribute UserReqDTO user) {

    }

    @DeleteMapping
    public ApiResponse<Users> deleteUser (@ModelAttribute UserReqDTO user) {

    }
}
