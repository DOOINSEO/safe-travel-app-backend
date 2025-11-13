package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Table(
        name = "post_images",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_post_images_order", columnNames = {"post_id", "img_order"})
        }
)
public class PostImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 하나의 게시글에 여러개의 이미지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Posts post;

    @Column(nullable = false)
    private String imgPath;

    // 이미지 순서
    @Column(name = "img_order", nullable = false)
    private Integer order;

}
