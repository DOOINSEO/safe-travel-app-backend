package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostLikeReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostLikeResDTO;

import java.util.List;

//  - PostLikeService
//  createPostLike : 추천 , input( PostLikeReqDTO ) , output( response? )
//  getPostLikeList : 추천 목록 조회(이용자 본인) , input( userId ) , output( PostResDTO? List<PostLikeResDTO>? )
//  getPostLike : 추천 여부 조회 , input( postId, userId ) , output( boolean? PostLikeResDTO? )
//  deletePostLike : 추천 삭제 , input( postId, userId ) , output( response? )
//  countPostLike : 추천 집계 , input( postId ) , output( Integer count )

public interface PostLikeService {
    public PostLikeResDTO create(Long userId, PostLikeReqDTO postLikeReqDTO);
    public PostLikeResDTO get(Long userId, Long postId);
    public PostLikeResDTO delete(Long userId, Long postId);
    public Integer count(Long postId);
}
