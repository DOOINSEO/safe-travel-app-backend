package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "locations",
        indexes = {
                @Index(name = "idx_region_code", columnList = "region_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", length = 8, nullable = false)
    private String countryCode;

    @Column(name = "country_name", length = 120, nullable = false)
    private String countryName;

    @Column(name = "region_code", length = 8, nullable = false, unique = true)
    private String regionCode;

    @Column(name = "region_name", length = 120, nullable = false)
    private String regionName;

    // `safety-stage` 컬럼 매핑 (하이픈 때문에 name 꼭 지정)
    @Column(
            name = "safety_stage",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    private Integer safetyStage;
}