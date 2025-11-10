package com.webkit.travel_safety_backend.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id @GeneratedValue
    private Long id;

    private String loginId;

    private String pwHash;

    private String name;

    private String phone;

    private String nickname;

    private Boolean alarmEnabled = true;
}
