package com.webkit.travel_safety_backend.domain.model.entity.ai;

import com.webkit.travel_safety_backend.domain.model.entity.Locations;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "risk_evaluate",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_scope_region",
                        columnNames = {"scope", "region_code"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ENUM('COUNTRY', 'REGION') DEFAULT 'REGION'
    @Enumerated(EnumType.STRING)
    @Column(
            name = "scope",
            nullable = false,
            columnDefinition = "ENUM('COUNTRY','REGION') DEFAULT 'REGION'"
    )
    @Builder.Default
    private Scope scope = Scope.REGION;

    // FK: region(region_code) (또는 locations.region_code 사용 시 DDL 맞춰주기)
    @Column(name = "region_code", length = 8)
    private String regionCode;

    // DECIMAL(5,3)
    @Column(name = "risk_score", precision = 5, scale = 3, nullable = false)
    private BigDecimal riskScore;

    // ENUM('LOW','MODERATE','HIGH','EXTREME')
    @Enumerated(EnumType.STRING)
    @Column(
            name = "risk_level",
            nullable = false,
            columnDefinition = "ENUM('LOW','MODERATE','HIGH','EXTREME')"
    )
    private RiskLevel riskLevel;

    // TINYINT UNSIGNED
    @Column(
            name = "safety_stage",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    private Integer safetyStage;

    // DB에서 DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
    @Column(
            name = "updated_at",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)"
    )
    private LocalDateTime updatedAt;

    // JSON 문자열로 매핑 (파서/컨버터는 서비스 레이어에서 처리)
    @Column(name = "details_json", columnDefinition = "JSON")
    private String detailsJson;

    // =========================
    // ENUM 정의
    // =========================
    public enum Scope {
        COUNTRY,
        REGION
    }

    public enum RiskLevel {
        LOW,
        MODERATE,
        HIGH,
        EXTREME
    }

    // =========================
    // (옵션) Location 연관관계
    // =========================
    // FK를 Location 엔티티에 연결하고 싶으면, region_code 기준으로 이렇게도 사용 가능
    // DDL에서 REFERENCES region(region_code) → 실제 테이블명이 locations면 거기에 맞춰서 수정해야 함.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "region_code",
            referencedColumnName = "region_code",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_risk_region")
    )
    private Locations region;
}