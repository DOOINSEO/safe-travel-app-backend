package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "locations",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_country_region_code",
                columnNames = {"country_code, region_code"})
        })
public class Locations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String regionCode;

    @Column(nullable = false)
    private String region;
}
