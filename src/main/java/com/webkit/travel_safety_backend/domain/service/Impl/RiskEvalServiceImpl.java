package com.webkit.travel_safety_backend.domain.service.Impl;

import com.webkit.travel_safety_backend.domain.model.dto.res.RiskEvalResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskEvaluate;
import com.webkit.travel_safety_backend.domain.model.mapper.RiskEvalMapper;
import com.webkit.travel_safety_backend.domain.repository.RiskEvalRepository;
import com.webkit.travel_safety_backend.domain.service.Interface.RiskEvalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RiskEvalServiceImpl implements RiskEvalService {
    private final RiskEvalRepository repo;
    private final RiskEvalMapper mapper;

    @Override
    public RiskEvalResDTO get(String regionCode) {
        RiskEvaluate risk = repo.findByRegionCode(regionCode).orElseThrow(()->new EntityNotFoundException("risk"));
        return mapper.toRes(risk);
    }
}
