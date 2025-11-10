package com.webkit.travel_safety_backend.domain.service.Impl;


import com.webkit.travel_safety_backend.domain.model.dto.req.PostImageReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostImageResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.PostImages;
import com.webkit.travel_safety_backend.domain.model.entity.Posts;
import com.webkit.travel_safety_backend.domain.model.mapper.PostImageMapper;
import com.webkit.travel_safety_backend.domain.repository.PostImageRepository;
import com.webkit.travel_safety_backend.domain.repository.PostRepository;
import com.webkit.travel_safety_backend.domain.service.FileStorageService;
import com.webkit.travel_safety_backend.domain.service.Interface.PostImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostImageServiceImpl implements PostImageService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FileStorageService fileStorageService;
    private final PostImageMapper mapper;


    @Override
    public void create(List<PostImageReqDTO> images, Long postId) {
        if (images == null || images.isEmpty()) return;
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post"));

        int autoOrder = 0;
        for (PostImageReqDTO i : images) {
            if (i.getFile() == null || i.getFile().isEmpty()) continue;
            int ord = (i.getOrder() == null) ? autoOrder++ : i.getOrder();
            String path = fileStorageService.saveImage(i.getFile(), post.getId(), ord);
            PostImages img = PostImages.builder()
                    .post(post)
                    .imgPath(path)
                    .order(ord)
                    .build();
            postImageRepository.save(img);
        }
    }

    @Override
    public List<PostImageResDTO> getList(Long postId) {
        return postImageRepository.findByPost_IdOrderByOrderAsc(postId).stream().map(mapper::toRes).toList();
    }

    @Override
    public void delete(Long imageId) {
        PostImages img = postImageRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("postImage"));
        postImageRepository.delete(img);

    }
}
