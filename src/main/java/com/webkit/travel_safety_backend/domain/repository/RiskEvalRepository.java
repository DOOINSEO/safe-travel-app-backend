package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskEvalRepository extends JpaRepository<RiskEvaluate, Long> {
    Optional<RiskEvaluate> findByRegionCode(String regionCode);
}
