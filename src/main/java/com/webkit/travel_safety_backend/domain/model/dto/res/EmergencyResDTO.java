package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class EmergencyResDTO {
    private Long userId;

    private String message;

    private String phone;
}
