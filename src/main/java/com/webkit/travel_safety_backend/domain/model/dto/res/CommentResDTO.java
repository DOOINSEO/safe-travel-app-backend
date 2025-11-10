package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResDTO {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String userNickname;
    private String content;
    private LocalDateTime createdAt;
}
