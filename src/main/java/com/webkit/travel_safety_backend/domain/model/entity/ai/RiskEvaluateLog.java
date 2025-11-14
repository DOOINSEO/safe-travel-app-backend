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
                @Index(name = "idx_scope_region_time",
                        columnList = "scope, region_code, created_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RiskEvaluateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ENUM('COUNTRY', 'REGION')
    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 16)
    private RiskScope scope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "region_code",
            referencedColumnName = "region_code",
            foreignKey = @ForeignKey(name = "fk_risklog_region")
    )
    private Locations locations;

    @Column(name = "risk_score", nullable = false, precision = 5, scale = 3)
    private BigDecimal riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 16)
    private RiskLevel riskLevel;

    @Column(name = "safety_stage", nullable = false)
    private Short safetyStage;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "details_json", columnDefinition = "json")
    private String detailsJson;

}
