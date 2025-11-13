package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPost_IdOrderByCreatedAtAsc(Long postId);

    List<Comments> findByUser_IdOrderByCreatedAtDesc(Long userId);

    Integer countByPost_Id(Long postId);
}
