package com.webkit.travel_safety_backend.domain.controller;

import com.webkit.travel_safety_backend.domain.model.dto.req.EmergencyReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.EmergencyResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.service.EmergencyService;
import com.webkit.travel_safety_backend.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/emergency")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyService emergencyService;

    @GetMapping
    public ResponseEntity<ApiResponse<EmergencyResDTO>> getEmergency(@AuthenticationPrincipal Users user) {
        EmergencyResDTO emergencyResDTO = emergencyService.getEmergency(user.getId());
        return ResponseEntity.ok(ApiResponse.success(emergencyResDTO));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmergencyResDTO>> postEmergency(@AuthenticationPrincipal Users user,
                                                                      @RequestBody EmergencyReqDTO emergencyReqDTO) {
        EmergencyResDTO emergencyResDTO = emergencyService.saveEmergency(user, emergencyReqDTO);
        return  ResponseEntity.ok(ApiResponse.success(emergencyResDTO));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<EmergencyResDTO>> putEmergency(@AuthenticationPrincipal Users user,
                                                                     @RequestBody EmergencyReqDTO emergencyReqDTO) {
        EmergencyResDTO emergencyResDTO = emergencyService.updateEmergency(user.getId(), emergencyReqDTO);
        return ResponseEntity.ok(ApiResponse.success(emergencyResDTO));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<EmergencyResDTO>> deleteEmergency(@AuthenticationPrincipal Users user) {
        EmergencyResDTO emergencyResDTO = emergencyService.deleteEmergency(user.getId());
        return ResponseEntity.ok(ApiResponse.success(emergencyResDTO));
    }
}
