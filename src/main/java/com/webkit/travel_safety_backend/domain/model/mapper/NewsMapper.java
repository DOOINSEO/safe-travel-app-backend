package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.res.NewsItemResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.ai.NewsItem;
import com.webkit.travel_safety_backend.global.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface NewsMapper {
    @Mapping(source = "id", target = "newsId")
    @Mapping(source = "titleKo", target = "title")
    @Mapping(source = "id", target = "content")
    @Mapping(source = "eventClass", target = "event")
    @Mapping(source = "regionCode", target = "regionCode")
    @Mapping(source = "publishedAt", target = "publishedAt")
    NewsItemResDTO toRes(NewsItem news);
}

//
//private Long NewsId;
//private String title;
//private String content;
//private Long categoryId;
//private Long locationId;
//private LocalDateTime publishedAt;