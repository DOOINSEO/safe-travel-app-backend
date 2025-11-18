package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.res.RiskEvalResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskEvaluate;
import com.webkit.travel_safety_backend.global.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface RiskEvalMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "regionCode", target = "regionCode")
    @Mapping(source = "riskScore", target = "riskScore")
    @Mapping(source = "riskLevel", target = "riskLevel")
    @Mapping(source = "safetyStage", target = "safetyStage")
    @Mapping(source = "updatedAt", target = "updatedAt")
    RiskEvalResDTO toRes(RiskEvaluate entity);
}
