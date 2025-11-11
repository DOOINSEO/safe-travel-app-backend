package com.webkit.travel_safety_backend.domain.model.entity;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id @GeneratedValue
    private Long id;

    private String accessToken;

    private String refreshToken;

    private Date expiration;

    private Long userId;

}
