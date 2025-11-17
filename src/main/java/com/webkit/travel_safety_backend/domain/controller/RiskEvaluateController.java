package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.res.RiskEvalResDTO;
import com.webkit.travel_safety_backend.domain.service.Interface.RiskEvalService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/risk")
@RestController
@RequiredArgsConstructor
public class RiskEvaluateController {
    private final RiskEvalService service;

    @GetMapping("/{regionCode}")
    public ApiResponse<RiskEvalResDTO> get(@PathVariable String regionCode){
        return ApiResponse.success(service.get(regionCode));
    }

}
