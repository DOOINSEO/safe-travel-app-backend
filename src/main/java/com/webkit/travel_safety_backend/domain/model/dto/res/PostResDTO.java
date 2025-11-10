package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResDTO {
    private Long postId;
    private Long userId;
    private String userNickname;
    private String content;
    private Long categoryId;
    private String categoryName;
    private Long locationId;
    // private String locationName;
    private List<PostImageResDTO> images;
    private Integer likeCount;
    private Boolean isLike;
    private LocalDateTime createdAt;
}
