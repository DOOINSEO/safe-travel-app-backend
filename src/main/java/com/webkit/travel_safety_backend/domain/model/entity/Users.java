package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_login_id", columnNames = "login_id"),
                @UniqueConstraint(name = "uk_users_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_users_nickname", columnNames = "nickname")
        })
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 16)
    private String loginId;

    @Column(name = "pw_hash", nullable = false, length = 255)
    private String pwHash;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 11)
    private String phone;

    @Column(nullable = false, length = 16)
    private String nickname;

    @Column(name = "alarm_enabled", nullable = false)
    private Boolean alarmEnabled = true;
}
