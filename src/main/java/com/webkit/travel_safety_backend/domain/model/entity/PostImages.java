package com.webkit.travel_safety_backend.domain.model.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PostImages {

    @Id @GeneratedValue
    private Long id;

    private String imgPath;

    // 이미지 순서
    private Long sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts post;



}
