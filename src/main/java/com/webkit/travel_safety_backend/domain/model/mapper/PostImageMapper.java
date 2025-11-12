package com.webkit.travel_safety_backend.domain.model.mapper;

import com.webkit.travel_safety_backend.domain.model.dto.res.PostImageResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.PostImages;
import com.webkit.travel_safety_backend.global.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PostImageMapper {
    @Mapping(source = "id", target = "imageId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "imgPath", target = "imgPath")
    @Mapping(source = "order", target = "order")
    PostImageResDTO toRes(PostImages e);
}
