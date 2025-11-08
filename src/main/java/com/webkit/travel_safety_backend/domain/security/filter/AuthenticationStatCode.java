package com.webkit.travel_safety_backend.domain.security.filter;

import org.springframework.stereotype.Component;

public class AuthenticationStatCode {
    public static final Integer VALID_STATE = 0;
    public static final Integer MANIPULATED_STATE = 1;
    public static final Integer EXPIRED_STATE = 2;
    public static final Integer UNSUPPORTED_STATE = 3;
    public static final Integer WRONG_STATE = 4;
    public static final Integer OTHER_STATE = 5;
}
