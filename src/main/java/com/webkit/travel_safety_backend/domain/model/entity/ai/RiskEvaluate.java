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
                @UniqueConstraint(name = "uk_scope_region", columnNames = {"scope", "region_code"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RiskEvaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ENUM('COUNTRY', 'REGION')
    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 16)
    private RiskScope scope;

    // FK (region.region_code)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "region_code",
            referencedColumnName = "region_code",
            foreignKey = @ForeignKey(name = "fk_risk_region")
    )
    private Locations locations;

    @Column(name = "risk_score", nullable = false, precision = 5, scale = 3)
    private BigDecimal riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 16)
    private RiskLevel riskLevel;

    @Column(name = "safety_stage", nullable = false)
    private Short safetyStage;

    // DB에서 DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
    @Column(name = "updated_at", nullable = false,
            insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
    private LocalDateTime updatedAt;

    // JSON은 일단 String으로 매핑 (필요하면 컨버터 or @Type 사용)
    @Column(name = "details_json", columnDefinition = "json")
    private String detailsJson;
}
