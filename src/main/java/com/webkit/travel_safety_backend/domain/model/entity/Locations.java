package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Locations {

    @Id @GeneratedValue
    private Long id;

    private String countryCode;

    private String country;

    private String regionCode;

    private String region;
}
