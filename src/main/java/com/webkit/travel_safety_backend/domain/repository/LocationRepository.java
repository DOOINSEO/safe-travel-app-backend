package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Locations, Long> {
}
