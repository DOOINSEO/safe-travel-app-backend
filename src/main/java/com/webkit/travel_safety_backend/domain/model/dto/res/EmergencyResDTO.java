package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class EmergencyResDTO {
    private Long userId;

    private String message;

    private String phone;
}
