package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikeResDTO {
    private Long postId;
    private Long userId;
    private Boolean isLike;
}
