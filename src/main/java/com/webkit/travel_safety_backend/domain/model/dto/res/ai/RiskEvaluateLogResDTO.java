package com.webkit.travel_safety_backend.domain.model.dto.res.ai;

import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskEvaluateLog;
import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskLevel;
import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskScope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluateLogResDTO {
    private Long id;
    private RiskScope scope;

    private String regionCode;
    private String regionName;

    private BigDecimal riskScore;
    private RiskLevel riskLevel;
    private Short safetyStage;

    private LocalDateTime createdAt;

    private String detailsJson;

    public static RiskEvaluateLogResDTO from(RiskEvaluateLog entity) {
        if (entity == null) return null;

        String regionCode = null;
        String regionName = null;
        if (entity.getLocations() != null) {
            regionCode = entity.getLocations().getRegionCode();
            regionName = entity.getLocations().getRegionName();
        }

        return RiskEvaluateLogResDTO.builder()
                .id(entity.getId())
                .scope(entity.getScope())
                .regionCode(regionCode)
                .regionName(regionName)
                .riskScore(entity.getRiskScore())
                .riskLevel(entity.getRiskLevel())
                .safetyStage(entity.getSafetyStage())
                .createdAt(entity.getCreatedAt())
                .detailsJson(entity.getDetailsJson())
                .build();
    }
}
