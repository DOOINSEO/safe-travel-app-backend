package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.res.CommentResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Comments;
import com.webkit.travel_safety_backend.global.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CommentMapper {
    @Mapping(source = "id", target = "commentId")
    @Mapping(source = "post.id", target="postId")
    @Mapping(source = "user.id", target="userId")
    @Mapping(source = "user.nickname", target="userNickname")
    CommentResDTO toRes(Comments e);
}
