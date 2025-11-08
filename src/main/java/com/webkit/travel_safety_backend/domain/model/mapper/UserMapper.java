package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/* MapStruct
* 객체 간 매퍼 구현을 자동화해주는 도구.
* 인터페이스에 @Mapper를 붙이면 MapStruct가 동작함.
* 기본적으로 구현체를 생성하지만 Bean으로 등록되지 않음. 따라서 ComponentModel 옵션을 통해 스프랑 빈으로 등록.
* 메서드 반환 타입이 있으면 반환 타입으로 새 객체 반환
* 메서드 반환 타입이 없으면 @MappingTarget이 붙은 타깃의 필드가 수정됨
* */

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    //UserReqDTO -> Users
    @Mapping(source = "password", target = "pwHash")
    @Mapping(target = "role", constant = "USER")
    Users toUsers(UserReqDTO userReqDTO);

    //Users -> UserResDTO
    UserResDTO toUserResDTO(Users user);

    // UserReqDTO -> 기존 User 수정
    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserReqDTO userReqDTO, @MappingTarget Users user);



}
