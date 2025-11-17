package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RiskEvalResDTO {
    private Long id;
    private String regionCode;
    private BigDecimal riskScore;
    private String riskLevel;
    private Integer safetyStage;
    private String detail;
    private LocalDateTime updatedAt;
}
