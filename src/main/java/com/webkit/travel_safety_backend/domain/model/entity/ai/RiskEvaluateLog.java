package com.webkit.travel_safety_backend.domain.model.entity.ai;

import com.webkit.travel_safety_backend.domain.model.entity.Locations;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "risk_evaluate_log",
        indexes = {
                @Index(
                        name = "idx_scope_region_time",
                        columnList = "scope, region_code, created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ENUM('COUNTRY','REGION') DEFAULT 'REGION'
    @Enumerated(EnumType.STRING)
    @Column(
            name = "scope",
            nullable = false,
            columnDefinition = "ENUM('COUNTRY','REGION') DEFAULT 'REGION'"
    )
    @Builder.Default
    private Scope scope = Scope.REGION;

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

    @Column(
            name = "safety_stage",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    private Integer safetyStage;

    // DB DEFAULT CURRENT_TIMESTAMP(6)
    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)"
    )
    private LocalDateTime createdAt;

    // JSON 문자열로 매핑
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
    // 실제 FK 테이블이 locations(region_code)라면
    // DDL의 REFERENCES region(region_code)도 locations로 맞춰주는 게 좋음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "region_code",
            referencedColumnName = "region_code",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_risklog_region")
    )
    private Locations region;
}
