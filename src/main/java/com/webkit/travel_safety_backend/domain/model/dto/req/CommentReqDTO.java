package com.webkit.travel_safety_backend.domain.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentReqDTO {
    @NotNull
    private Long postId;
    @NotBlank
    private String content;
}
