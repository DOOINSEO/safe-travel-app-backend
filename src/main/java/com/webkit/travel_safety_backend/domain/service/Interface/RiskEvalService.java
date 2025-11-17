package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.res.RiskEvalResDTO;

public interface RiskEvalService {
    public RiskEvalResDTO get(String regionCode);
}
