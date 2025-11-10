package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class PostImages {

    @Id @GeneratedValue
    private Long id;

    // 하나의 게시글에 여러개의 이미지
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Long post_id;

    private String imgPath;

    // 이미지 순서
    private int order;

}
