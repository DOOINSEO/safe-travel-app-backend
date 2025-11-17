package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.CommentReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.CommentResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.service.Interface.CommentService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
 * 1. Comment 생성 ( userId, postId )
 * 2. Comment 목록 ( postId )
 * 3. Comment 갱신 ( userId, postId )
 * 4. Comment 삭제 ( userId, postId )
 * */

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

//    private Long getUserId(){
//        return 1L;
//    }

    @PostMapping
    public ApiResponse<CommentResDTO> create(@AuthenticationPrincipal Users user,
                                             @RequestBody @Valid CommentReqDTO dto){
        return ApiResponse.success(commentService.create(user.getId(), dto));
    }

    @GetMapping
    public ApiResponse<List<CommentResDTO>> getList(@RequestParam Long postId){
        return ApiResponse.success(commentService.getList(postId));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResDTO> update(@AuthenticationPrincipal Users user,
                                             @PathVariable Long commentId,
                                             @Valid CommentReqDTO dto){
        return ApiResponse.success(commentService.update(user.getId(), commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal Users user,
                                    @PathVariable Long commentId){
        commentService.delete(user.getId(), commentId);
        return ApiResponse.success("deleted", null);
    }
}
