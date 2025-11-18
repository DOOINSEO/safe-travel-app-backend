package com.webkit.travel_safety_backend.domain.service;

import com.webkit.travel_safety_backend.domain.model.dto.req.EmergencyReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.EmergencyResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Emergency;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.EmergencyMapper;
import com.webkit.travel_safety_backend.domain.repository.EmergencyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;

    private final EmergencyMapper emergencyMapper;

    public EmergencyResDTO saveEmergency(Users user,
                                         EmergencyReqDTO emergencyReqDTO) {
        Emergency emergency = emergencyRepository.save(emergencyMapper.toEmergency(user, emergencyReqDTO));

        log.info("Emergency Saved Successfully");
        return emergencyMapper.toEmergencyResDTO(emergency);
    }

    public EmergencyResDTO updateEmergency(Long userId, EmergencyReqDTO emergencyReqDTO) {
        Emergency emergency = emergencyRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Emergency Not Found"));
        log.info("Emergency Found Successfully");

        if (!emergency.getPhone().equals(emergencyReqDTO.getPhone())) emergency.setPhone(emergencyReqDTO.getPhone());
        if (!emergency.getMessage().equals(emergencyReqDTO.getMessage())) emergency.setMessage(emergencyReqDTO.getMessage());

        emergencyRepository.save(emergency);
        log.info("Emergency Updated Successfully");

        return emergencyMapper.toEmergencyResDTO(emergency);
    }

    public EmergencyResDTO deleteEmergency(Long userId) {
        Emergency emergency = emergencyRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Emergency Not Found"));
        log.info("Emergency Found Successfully");

        emergencyRepository.delete(emergency);
        log.info("Emergency Deleted Successfully");
        return emergencyMapper.toEmergencyResDTO(emergency);
    }

    public EmergencyResDTO getEmergency(Users user) {
        try {
            Long userId = user.getId();
            Emergency emergency = emergencyRepository.findByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Emergency Not Found"));

            log.info("Emergency Found Successfully");
            return emergencyMapper.toEmergencyResDTO(emergency);
        } catch (EntityNotFoundException e) {
            log.info("Emergency Not Found");
            return new EmergencyResDTO(user.getId(), user.getNickname(), null, "", "");
        }
    }
}
