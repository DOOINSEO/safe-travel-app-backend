package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.CommentReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.CommentResDTO;
import com.webkit.travel_safety_backend.domain.service.Interface.CommentService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
 * 1. Comment 생성 ( userId, postId )
 * 2. Comment 목록 ( postId )
 * 3. Comment 갱신 ( userId, postId )
 * 4. Comment 삭제 ( userId, postId )
 * */

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private Long getUserId(){
        return 1L;
    }

    @PostMapping
    public ApiResponse<CommentResDTO> create(@RequestBody @Valid CommentReqDTO dto){
        return ApiResponse.success(commentService.create(getUserId(), dto));
    }

    @GetMapping
    public ApiResponse<List<CommentResDTO>> getList(@RequestParam Long postId){
        return ApiResponse.success(commentService.getList(postId));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResDTO> update(@PathVariable Long commentId, @Valid CommentReqDTO dto){
        return ApiResponse.success(commentService.update(getUserId(), commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(Long commentId){
        commentService.delete(getUserId(), commentId);
        return ApiResponse.success("deleted", null);
    }
}
