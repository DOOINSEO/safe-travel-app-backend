package com.webkit.travel_safety_backend.domain.service.Interface;


// Res, Req 다 바꾸고

//- PostService
//createPost : 게시글 등록 , input( PostReqDTO ) , output( response? PostResDTO? )
//getPostList : 게시글 목록 조회 , input( query ) , output( List<PostResDTO> )
//getPost : 게시글 조회 , input( postId ) , output( PostResDTO )
//updatePost : 게시글 수정 , input( PostReqDTO ) , output( response? PostResDTO? )
//deletePost : 게시글 삭제 , input( postId ) , output( response? )

import com.webkit.travel_safety_backend.domain.model.dto.req.PostReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostResDTO;
import org.springframework.data.domain.Page;

public interface PostService {
    public PostResDTO create(Long userId, PostReqDTO reqDTO);
    public PostResDTO get(Long userId, Long postId);
    Page<PostResDTO> getList(Long userId, Integer page, Integer size, String sort, Long categoryId, Long locationId, String q);
    public PostResDTO update(Long userId, Long postId, PostReqDTO reqDTO);
    public void delete(Long userId, Long postId);
}
