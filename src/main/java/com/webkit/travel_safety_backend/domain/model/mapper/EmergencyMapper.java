package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.req.EmergencyReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.EmergencyResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Emergency;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import jakarta.persistence.Id;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmergencyMapper {

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "message", source = "emergencyReqDTO.message")
    @Mapping(target = "phone", source = "emergencyReqDTO.phone")
    Emergency toEmergency(Users user, EmergencyReqDTO emergencyReqDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "nickname", source = "user.nickname")
    @Mapping(target = "emergencyId", source = "id")
    EmergencyResDTO toEmergencyResDTO(Emergency emergency);

//    EmergencyPhoneList toEmergencyPhoneList(EmergencyReqDTO emergencyReqDTO);
}
