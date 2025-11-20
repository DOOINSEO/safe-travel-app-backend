package com.webkit.travel_safety_backend.domain.model.entity;

import io.jsonwebtoken.security.Password;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String pwHash;

    private String name;

    private String phone;

    private String nickname;

    private Boolean alarmEnabled;
    
    @Enumerated(EnumType.STRING)
    private Role role;

}
