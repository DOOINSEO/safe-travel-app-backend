package com.webkit.travel_safety_backend.domain.model.dto.req;

import lombok.Data;

@Data
public class UserReqDTO {

    private String loginId;

    private String pwHash;

    private String name;

    private String phone;

    private String nickname;

    private Boolean alarmEnabled = true;
}
