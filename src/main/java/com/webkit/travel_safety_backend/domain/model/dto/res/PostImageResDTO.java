package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostImageResDTO {
    private Long imageId;
    private Long postId;
    private String filePath;
    private Integer order;
}
