package com.webkit.travel_safety_backend.domain.model.dto.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostImageReqDTO {
    private MultipartFile file;
    private String imgPath;
    private Integer order;
}
