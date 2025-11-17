package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.res.PostImageResDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Posts;
import com.webkit.travel_safety_backend.global.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface PostMapper {
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nickname", target = "userNickname")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.countryName", target = "country")
    @Mapping(source = "location.regionName", target = "region")
    @Mapping(source = "createdAt", target = "createdAt")
    PostResDTO toRes(Posts entity);
}