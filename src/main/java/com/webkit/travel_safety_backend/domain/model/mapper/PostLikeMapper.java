package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.res.PostLikeResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.PostLike;
import com.webkit.travel_safety_backend.global.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PostLikeMapper {
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "isLike", constant = "true")
    PostLikeResDTO toRes(PostLike e);
}
