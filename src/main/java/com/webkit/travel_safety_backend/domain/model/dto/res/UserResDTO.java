package com.webkit.travel_safety_backend.domain.model.dto.res;

import com.webkit.travel_safety_backend.domain.model.entity.Role;
import lombok.Data;

@Data
public class UserResDTO {
    private Long id;

    private String loginId;

    private String name;

    private String phone;

    private String nickname;

    private Boolean alarmEnabled;

    private Role role;

}
    