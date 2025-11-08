package com.webkit.travel_safety_backend.domain.security.custom;

import com.webkit.travel_safety_backend.domain.model.entity.Users;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Getter
    private final Users user;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRole().name();
            }
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPwHash();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

}
