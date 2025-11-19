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

    @PostMapping
    public ApiResponse<PostResDTO> create(@AuthenticationPrincipal Users user, @ModelAttribute  @Valid PostReqDTO dto) {
        log.info("REST request to create Post : {}", dto);
        return ApiResponse.success(postService.create(user.getId(), dto));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResDTO> get(@AuthenticationPrincipal Users user, @PathVariable Long postId) {
        log.info("REST request to get Post : {}", postId);
        return ApiResponse.success(postService.get(user.getId(), postId));
    }

    @GetMapping
    public ApiResponse<Page<PostResDTO>> getList(@RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(required = false) String regionCode,
                                                 @RequestParam(required = false) Long categoryId,
                                                 @RequestParam(required = false) String q,
                                                 @AuthenticationPrincipal Users user){
        log.info("REST request to get Posts : {}", page);
        return ApiResponse.success(postService.getList(user.getId(), page, size, sort, regionCode, categoryId, q));
    }


    @PutMapping("/{postId}")
    public ApiResponse<PostResDTO> update(@AuthenticationPrincipal Users user, @PathVariable Long postId, @ModelAttribute @Valid PostReqDTO dto) {
        log.info("REST request to update Post : {}", dto);
        return ApiResponse.success(postService.update(user.getId(), postId, dto));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal Users user, @PathVariable Long postId) {
        log.info("REST request to delete Post : {}", postId);
        postService.delete(user.getId(), postId);
        return ApiResponse.success("deleted", null);
    }
}