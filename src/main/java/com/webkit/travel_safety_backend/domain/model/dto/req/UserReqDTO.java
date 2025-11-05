package com.webkit.travel_safety_backend.domain.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReqDTO {

    private String loginId;

    private String pwHash;

    private String name;

    private String phone;

    private String nickname;

    private Boolean alarmEnabled = true;
}
