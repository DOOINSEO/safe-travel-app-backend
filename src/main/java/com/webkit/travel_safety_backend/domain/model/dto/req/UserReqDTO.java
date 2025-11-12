package com.webkit.travel_safety_backend.domain.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReqDTO {

    private String loginId;

    private String password;

    private String name;

    private String phone;

    private String nickname;

//    private String alarmEnabled;

    private Boolean alarmEnabled;
}
