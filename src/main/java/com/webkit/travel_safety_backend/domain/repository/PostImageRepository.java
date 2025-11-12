package com.webkit.travel_safety_backend.domain.repository;

import com.webkit.travel_safety_backend.domain.model.entity.PostImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImages, Long> {
    List<PostImages> findByPost_IdOrderByOrderAsc(Long postId);

    void deleteByPost_Id(Long postId);

    void flush(); // 명시적 flush 사용 (update 시 유니크 충돌 방지용)
}
