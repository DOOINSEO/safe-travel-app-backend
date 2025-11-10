package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
public class Posts {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 하나의 카테고리에 여러개의 게시글
    @ManyToOne
    @JoinColumn(name = "category_id")
    private PostCategory category;

    // 하나의 유저에 여러개의 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    // 하나의 지역에 여러개의 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Locations location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImages> images = new ArrayList<>();

}


