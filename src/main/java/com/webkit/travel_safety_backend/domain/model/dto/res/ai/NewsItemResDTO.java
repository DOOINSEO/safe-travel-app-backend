package com.webkit.travel_safety_backend.domain.model.dto.res.ai;

import com.webkit.travel_safety_backend.domain.model.entity.ai.NewsEventClass;
import com.webkit.travel_safety_backend.domain.model.entity.ai.NewsItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsItemResDTO {

    private Long id;

    private String regionName;
    private String regionCode;

    private String title;
    private String titleKo;

    private String sourceName;
    private Short reliability;

    private NewsEventClass eventClass;

    private String url;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;

    public static NewsItemResDTO from(NewsItem entity) {
        if (entity == null) return null;

        return NewsItemResDTO.builder()
                .id(entity.getId())
                .regionName(entity.getRegionName())
                .regionCode(entity.getRegionCode())
                .title(entity.getTitle())
                .titleKo(entity.getTitleKo())
                .sourceName(entity.getSourceName())
                .reliability(entity.getReliability())
                .eventClass(entity.getEventClass())
                .url(entity.getUrl())
                .publishedAt(entity.getPublishedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
