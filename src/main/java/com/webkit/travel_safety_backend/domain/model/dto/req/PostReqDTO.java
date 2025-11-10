package com.webkit.travel_safety_backend.domain.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostReqDTO {
    @NotBlank
    private String content;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long locationId;

    private List<PostImageReqDTO> images;
}
