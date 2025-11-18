package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LocationRepository extends JpaRepository<Locations, Long> {
    Optional<Locations> findByRegionCode(String regionCode);
}
