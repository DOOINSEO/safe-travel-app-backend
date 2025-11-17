package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.res.RiskEvalResDTO;

// - RiskEvalService
// get : 위험도 조회 (지역별), input( regionCode ) , output ( resDTO )
public interface RiskEvalService {
    public RiskEvalResDTO get(String regionCode);
}
