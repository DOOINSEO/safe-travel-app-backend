package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LocationRepository extends JpaRepository<Locations, Long> {
    Optional<Locations> findByRegionCode(String regionCode);
}
