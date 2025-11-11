package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PostImages {

    @Id @GeneratedValue
    private Long id;

    private String imgPath;

    // 이미지 순서
    private Long order;


}
