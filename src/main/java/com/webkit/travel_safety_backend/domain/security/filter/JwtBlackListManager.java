//package com.webkit.travel_safety_backend.domain.security.filter;
//
//import lombok.RequiredArgsConstructor;
//import org.antlr.v4.runtime.Token;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
//@Component
//@RequiredArgsConstructor
//public class JwtBlackListManager {
//
//    private final JwtUtils jwtUtils;
//    private final ArrayList<String> blackList = new ArrayList<>();
//
//    public void addJwtInBlackList(String token) {
//        blackList.add(token);
//    }
//
//    public Boolean isBlackList(String token) {
//        return blackList.contains(token);
//    }
//
//    public void removeJwtFromBlackList(String token) {
//        blackList.remove(token);
//    }
//
//    public void removeJwt() {
//        blackList.forEach(
//                token -> {
//                    if (jwtUtils.isExpired(token))
//                        removeJwtFromBlackList(token);
//                }
//        );
//    }
//}
