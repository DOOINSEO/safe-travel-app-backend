package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.res.NewsItemResDTO;
import org.springframework.data.domain.Page;

public interface NewsService {
    public NewsItemResDTO get(Long newsId);
    public Page<NewsItemResDTO> getList(Integer page, Integer size, Boolean sort, String event, String regionCode, String q);
}
