package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

}
