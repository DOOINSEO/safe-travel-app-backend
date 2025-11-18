package com.webkit.travel_safety_backend.domain.model.dto.req;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class EmergencyReqDTO {

    private String message;

    private String phone;
}
