package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.mapping.ToOne;

import java.time.LocalDateTime;

@Entity
@Data
public class Posts {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private PostCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Locations location;
}
