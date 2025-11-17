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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Phnom Penh" / 전국 공지는 NULL
    @Column(name = "region_name", length = 120)
    private String regionName;

    // 예: "KHM-12"
    @Column(name = "region_code", length = 8)
    private String regionCode;

    @Column(name = "title", length = 600, nullable = false)
    private String title;

    @Column(name = "title_ko", length = 600)
    private String titleKo;

    // "Kampuchea News", "KR-MOFA" 등
    @Column(name = "source_name", length = 160, nullable = false)
    private String sourceName;

    // TINYINT UNSIGNED DEFAULT 1
    @Column(
            name = "reliability",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED DEFAULT 1"
    )
    private Integer reliability;

    // ENUM('crime','accident','gov_notice')
    @Enumerated(EnumType.STRING)
    @Column(name = "event_class", nullable = false)
    private EventClass eventClass;

    // URL 중복 방지
    @Column(name = "url", length = 700, nullable = false, unique = true)
    private String url;

    // 기사 발행 시각 (UTC 권장)
    @Column(name = "published_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime publishedAt;

    // DB에서 DEFAULT CURRENT_TIMESTAMP(6)
    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)"
    )
    private LocalDateTime createdAt;

    // =========================
    // ENUM 정의
    // =========================
    // DB ENUM 값과 정확히 맞추려고 소문자/스네이크 그대로 사용
    public enum EventClass {
        crime,
        accident,
        gov_notice
    }
}