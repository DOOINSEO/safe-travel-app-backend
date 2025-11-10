package com.webkit.travel_safety_backend.domain.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostImageReqDTO {
    @NotNull
    private MultipartFile file;
    private Integer order;
}
