package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostImageReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostImageResDTO;

import java.util.List;

//  - PostImageService
//  createPostImage : 이미지 등록 , input( List<ImageReqDTO> ) , output( response? )
//  getPostImageList : 이미지 조회 , input( postId ) , output( List<ImageResDTO> )
//  deletePostImage : 이미지 삭제 , input( imageId ) , output( response? )
public interface PostImageService {
    public void create(List<PostImageReqDTO> images, Long postId);
    public List<PostImageResDTO> getList(Long postId);
    public void delete(Long imageId);
}
