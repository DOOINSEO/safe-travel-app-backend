package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsItemResDTO {
    private Long newsId;
    private String title;
    private String content;
    private String event;
    private String regionCode;
    private LocalDateTime publishedAt;
}
