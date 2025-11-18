package com.webkit.travel_safety_backend.domain.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvalResDTO {
    // 기본 필드
    private Integer id;
    private String regionCode;
    private BigDecimal riskScore;
    private String riskLevel;
    private Integer safetyStage;
    private LocalDateTime updatedAt;

    // detail.sources.MOFA
    private BigDecimal mofaRisk;
    private Integer mofaStage;

    // detail.sources.NEWS
    private BigDecimal newsR;
    private Map<String, Long> newsCategoryCounts;

    // detail.sources.GDACS
    private BigDecimal gdacsR;
    private Map<String, Long> gdacsEpisodeCounts;

    private String resultContext;
}
//
//
//riskScore
//riskLevel
//safetyStage
//detail.source.MOFA.risk
//detail.source.MOFA.stage
//detail.source.NEWS.R
//detail.source.NEWS.items 의 category별 건수
//detail.source.GDACS.R
//detail.source.GDACS.events의 episodeId별 건수
//updatedAt