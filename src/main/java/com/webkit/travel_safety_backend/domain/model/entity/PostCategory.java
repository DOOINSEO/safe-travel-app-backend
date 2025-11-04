package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PostCategory {

    @Id @GeneratedValue
    private Long id;

    // 한글명
    private String name;

    // 영문명
    private String code;
}
