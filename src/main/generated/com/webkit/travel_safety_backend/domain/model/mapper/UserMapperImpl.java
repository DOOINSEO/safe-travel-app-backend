package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.req.UserReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.UserResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Role;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T15:08:24+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users toUsers(UserReqDTO userReqDTO) {
        if ( userReqDTO == null ) {
            return null;
        }

        Users.UsersBuilder users = Users.builder();

        users.pwHash( userReqDTO.getPassword() );
        users.loginId( userReqDTO.getLoginId() );
        users.name( userReqDTO.getName() );
        users.phone( userReqDTO.getPhone() );
        users.nickname( userReqDTO.getNickname() );
        users.alarmEnabled( userReqDTO.getAlarmEnabled() );

        users.role( Role.USER );

        return users.build();
    }

    @Override
    public UserResDTO toUserResDTO(Users user) {
        if ( user == null ) {
            return null;
        }

        UserResDTO userResDTO = new UserResDTO();

        userResDTO.setId( user.getId() );
        userResDTO.setLoginId( user.getLoginId() );
        userResDTO.setName( user.getName() );
        userResDTO.setPhone( user.getPhone() );
        userResDTO.setNickname( user.getNickname() );
        userResDTO.setAlarmEnabled( user.getAlarmEnabled() );
        userResDTO.setRole( user.getRole() );

        return userResDTO;
    }

    @Override
    public void updateUserFromDto(UserReqDTO userReqDTO, Users user) {
        if ( userReqDTO == null ) {
            return;
        }

        if ( userReqDTO.getLoginId() != null ) {
            user.setLoginId( userReqDTO.getLoginId() );
        }
        if ( userReqDTO.getName() != null ) {
            user.setName( userReqDTO.getName() );
        }
        if ( userReqDTO.getPhone() != null ) {
            user.setPhone( userReqDTO.getPhone() );
        }
        if ( userReqDTO.getNickname() != null ) {
            user.setNickname( userReqDTO.getNickname() );
        }
        if ( userReqDTO.getAlarmEnabled() != null ) {
            user.setAlarmEnabled( userReqDTO.getAlarmEnabled() );
        }
    }
}
