package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "post_category",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_post_category_code", columnNames = "code"),
                @UniqueConstraint(name = "uk_post_category_name", columnNames = "name")
        })
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(nullable = false, length = 20)
    private String code;
}
