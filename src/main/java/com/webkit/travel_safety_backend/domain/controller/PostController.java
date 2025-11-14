package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.CommentResDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.service.Interface.CommentService;
import com.webkit.travel_safety_backend.domain.service.Interface.PostService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.FileAlreadyExistsException;
import java.util.List;

/*
 * 1. Post 생성 ( Post, PostImages )
 * 2. Post 조회 ( 상세조회 : title, author(user_nickname), content, createdAt, category(post_category_name), location(country, region), images(PostImages_imgPath), likeCounts(???) )
 * 3. Post 목록 ( 전체 , 필터링(지역별 || 카테고리별 ) , 정렬(추천순 || 최신순) , 검색(제목 || 내용 || 종합) )
 * 4. Post 갱신 ( Post, PostImages )
 * 5. Post 삭제 ( userId, PostId )
 *
 * */

@Slf4j
@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private Long getUserId() {
        return 1L;
    }

    @PostMapping
    public ApiResponse<PostResDTO> create(@AuthenticationPrincipal Users user, @ModelAttribute @Valid PostReqDTO dto) {
        return ApiResponse.success(postService.create(user.getId(), dto));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResDTO> get(@PathVariable Long postId) {
        return ApiResponse.success(postService.get(getUserId(), postId));
    }

    @GetMapping
    public ApiResponse<Page<PostResDTO>> getList(@RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(required = false) Long locationId,
                                                 @RequestParam(required = false) Long categoryId,
                                                 @RequestParam(required = false) String q){
        return ApiResponse.success(postService.getList(getUserId(), page, size, sort, locationId, categoryId, q));
    }


    @PutMapping("/{postId}")
    public ApiResponse<PostResDTO> update(@PathVariable Long postId, @ModelAttribute @Valid PostReqDTO dto) {
        return ApiResponse.success(postService.update(getUserId(), postId, dto));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> delete(@PathVariable Long postId) {
        postService.delete(getUserId(), postId);
        return ApiResponse.success("deleted", null);
    }
}
