package com.webkit.travel_safety_backend.domain.model.dto.res.ai;

import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskEvaluate;
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
public class RiskEvaluateResDTO {

    private Long id;
    private RiskScope scope;

    private String regionCode;
    private String regionName;

    private BigDecimal riskScore;
    private RiskLevel riskLevel;
    private Short safetyStage;

    private LocalDateTime updatedAt;

    // 필요하다면 문자열 JSON 그대로 응답, 아니면 Map<String,Object> 등으로 변경 가능
    private String detailsJson;

    public static RiskEvaluateResDTO from(RiskEvaluate entity) {
        if (entity == null) return null;

        String regionCode = null;
        String regionName = null;
        if (entity.getLocations() != null) {
            regionCode = entity.getLocations().getRegionCode();
            regionName = entity.getLocations().getRegionName();
        }

        return RiskEvaluateResDTO.builder()
                .id(entity.getId())
                .scope(entity.getScope())
                .regionCode(regionCode)
                .regionName(regionName)
                .riskScore(entity.getRiskScore())
                .riskLevel(entity.getRiskLevel())
                .safetyStage(entity.getSafetyStage())
                .updatedAt(entity.getUpdatedAt())
                .detailsJson(entity.getDetailsJson())
                .build();
    }
}
