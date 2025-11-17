package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.res.NewsItemResDTO;
import com.webkit.travel_safety_backend.domain.service.Interface.NewsService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/news")
@RestController
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;

    @GetMapping("/{newsId}")
    public ApiResponse<NewsItemResDTO> get(@PathVariable Long newsId){
        return ApiResponse.success(service.get(newsId));
    }

    @GetMapping
    public ApiResponse<Page<NewsItemResDTO>> getList(@RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false, defaultValue = "True") Boolean sort,
                                                     @RequestParam(required = false) String event,
                                                     @RequestParam(required = false) String regionCode,
                                                     @RequestParam(required = false) String q){
        return ApiResponse.success(service.getList(page, size, sort, event, regionCode, q));
    }
}

