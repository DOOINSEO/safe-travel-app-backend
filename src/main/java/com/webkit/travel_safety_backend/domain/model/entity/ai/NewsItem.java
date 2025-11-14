package com.webkit.travel_safety_backend.domain.model.entity.ai;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "news_item",
        indexes = {
                @Index(name = "idx_region_code", columnList = "region_code"),
                @Index(name = "idx_region_time", columnList = "region_name, published_at"),
                @Index(name = "idx_rel_time", columnList = "reliability, published_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Phnom Penh" 같은 문자열, 전국 공지는 NULL
    @Column(name = "region_name", length = 120)
    private String regionName;

    // e.g. "KHM-12"
    @Column(name = "region_code", length = 8)
    private String regionCode;

    @Column(name = "title", length = 600, nullable = false)
    private String title;

    @Column(name = "title_ko", length = 600)
    private String titleKo;

    @Column(name = "source_name", length = 160, nullable = false)
    private String sourceName;

    // TINYINT UNSIGNED
    @Column(name = "reliability", nullable = false)
    private Short reliability;

    // ENUM('crime','accident','gov_notice')
    @Enumerated(EnumType.STRING)
    @Column(name = "event_class", nullable = false, length = 32)
    private NewsEventClass eventClass;

    @Column(name = "url", length = 700, nullable = false, unique = true)
    private String url;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
