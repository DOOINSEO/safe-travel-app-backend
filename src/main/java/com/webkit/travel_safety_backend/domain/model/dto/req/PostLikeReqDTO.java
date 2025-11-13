package com.webkit.travel_safety_backend.domain.model.dto.req;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostLikeReqDTO {
    @NotNull
    private Long postId;

    @NotNull
    private Long userId;
}
