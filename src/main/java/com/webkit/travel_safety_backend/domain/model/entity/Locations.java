package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_region_code", columnList = "region_code")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    // `safety-stage` TINYINT UNSIGNED NOT NULL
    @Column(name = "safety_stage", nullable = false)
    private Short safetyStage;
}
