package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long>, JpaSpecificationExecutor<Posts> {
}
