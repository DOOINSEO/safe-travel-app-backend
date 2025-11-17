package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostLikeReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostLikeResDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.service.Interface.PostLikeService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/*
 * 1. Like 생성 ( dto )
 * 3. Like 조회 ( postId )
 * 4. like 삭제 ( postId )
 * */

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    private Long getUserId(){
        return 1L;
    }

    @PostMapping
    public ApiResponse<PostLikeResDTO> create(@AuthenticationPrincipal Users user,
                                              @RequestBody @Valid PostLikeReqDTO reqDTO){
        return ApiResponse.success(postLikeService.create(user.getId(), reqDTO));
    }

    @GetMapping
    public ApiResponse<PostLikeResDTO> get(@AuthenticationPrincipal Users user,
                                           @RequestParam Long postId){
        return ApiResponse.success(postLikeService.get(user.getId(), postId));
    }

    @DeleteMapping
    public ApiResponse<Void> delete(@AuthenticationPrincipal Users user,
                                    @RequestParam Long postId){
        postLikeService.delete(user.getId(), postId);
        return ApiResponse.success("deleted", null);
    }
}
