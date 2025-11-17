package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.res.NewsItemResDTO;
import org.springframework.data.domain.Page;


// NewsService
// get : 게시글 조회 , input( newsId ) , output( response? PostResDTO? )
// getList : 게시글 목록 조회 , input( query ) , output( Page<PostResDTO> )

public interface NewsService {
    public NewsItemResDTO get(Long newsId);
    public Page<NewsItemResDTO> getList(Integer page, Integer size, Boolean sort, String event, String regionCode, String q);
}
