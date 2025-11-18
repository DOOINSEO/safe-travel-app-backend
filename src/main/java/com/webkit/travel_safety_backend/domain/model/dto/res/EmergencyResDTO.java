package com.webkit.travel_safety_backend.domain.model.dto.res;

import com.webkit.travel_safety_backend.domain.model.entity.Emergency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class EmergencyResDTO {
    private Long userId;

    private String nickname;

    private Long emergencyId;

    private String message;

    private String phone;
}
