package com.webkit.travel_safety_backend.domain.service.Impl;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostImageReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.req.PostReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostImageResDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.*;
import com.webkit.travel_safety_backend.domain.model.mapper.PostMapper;
import com.webkit.travel_safety_backend.domain.repository.*;
import com.webkit.travel_safety_backend.domain.service.FileStorageService;
import com.webkit.travel_safety_backend.domain.service.Interface.PostService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final LocationRepository locationRepository;
    private final FileStorageService fileStorageService;
    private final PostMapper mapper;

    @Override
    public PostResDTO create(Long userId, PostReqDTO reqDTO) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user"));
        PostCategory postCategory = postCategoryRepository.findById(reqDTO.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("category"));
        Locations location = locationRepository.findById(reqDTO.getLocationId()).orElseThrow(() -> new EntityNotFoundException("location"));

        Posts post = Posts.builder()
                .content(reqDTO.getContent())
                .user(user)
                .category(postCategory)
                .location(location)
                .build();
        Posts saved = postRepository.save(post);

        if(reqDTO.getImages() != null && !reqDTO.getImages().isEmpty()){
            int autoOrder = 0;

            for (PostImageReqDTO i : reqDTO.getImages()){
                if(i.getFile() == null || i.getFile().isEmpty()) continue;

                int ord = (i.getOrder() == null) ? autoOrder++ : i.getOrder();
                String path = fileStorageService.saveImage(i.getFile(), saved.getId(), ord);
                PostImages img = PostImages.builder()
                        .post_id(saved.getId())
                        .imgPath(path)
                        .order(ord)
                        .build();
                postImageRepository.save(img);
            }
        }
        return assembleRes(userId, post);
    }

    @Override
    public PostResDTO get(Long userId, Long postId) {
        Posts post = postRepository.findById(postId).orElseThrow(()->new EntityNotFoundException("post"));
        return assembleRes(userId, post);
    }

    @Override
    public Page<PostResDTO> getList(Long userId, Integer page, Integer size, String sort, Long categoryId, Long locationId, String q) {
        // sorting
        Pageable pageable = PageRequest.of(page == null ? 0 : page, Math.min(size == null ? 10 : size, 20),
                "likeCount".equalsIgnoreCase(sort)
                        ? Sort.by(Sort.Direction.DESC, "likeCount").and(Sort.by(Sort.Direction.DESC, "id"))
                        : Sort.by(Sort.Direction.DESC, "id"));

        // filtering
        Specification<Posts> spec = Specification.allOf();
        if (locationId != null)
            spec = spec.and((r, qy, cb) -> cb.equal(r.get("location").get("id"), locationId));
        if (categoryId != null)
            spec = spec.and((r, qy, cb) -> cb.equal(r.get("category").get("id"), categoryId));
        if (q != null && !q.isBlank()) {
            String like = "%" + q.trim() + "%";
            spec = spec.and((r, qy, cb) -> cb.like(r.get("content"), like));
        }
        Page<Posts> paged = postRepository.findAll(spec, pageable);

        java.util.List<PostResDTO> content = paged.getContent().stream()
                .map(p -> assembleRes(userId, p)).toList();

        return new PageImpl<>(content, pageable, paged.getTotalElements());
    }

    @Override
    public PostResDTO update(Long userId, Long postId, PostReqDTO reqDTO) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post"));
        if (!Objects.equals(post.getUser().getId(), userId))
            throw new SecurityException("no Auth");
        if (reqDTO.getContent() != null)
            post.setContent(reqDTO.getContent());
        if (reqDTO.getCategoryId() != null)
            post.setCategory(postCategoryRepository.findById(reqDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("category")));
        if (reqDTO.getLocationId() != null)
            post.setLocation(locationRepository.findById(reqDTO.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("location")));
        postRepository.save(post);

        if (reqDTO.getImages() != null) {
            postImageRepository.deleteByPost_Id(postId);
            if(!reqDTO.getImages().isEmpty()){
                int autoOrder = 0;
                for(PostImageReqDTO i : reqDTO.getImages()){
                    if(i.getFile() == null || i.getFile().isEmpty()) continue;
                    int ord = (i.getOrder() == null) ? autoOrder++ : i.getOrder();
                    String path = fileStorageService.saveImage(i.getFile(), post.getId(), ord);
                    PostImages img = PostImages.builder()
                            .post_id(postId)
                            .imgPath(path)
                            .order(ord)
                            .build();
                    postImageRepository.save(img);
                }
            }
        }
        return assembleRes(userId, post);
    }

    @Override
    public void delete(Long userId, Long postId) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post"));
        if(!Objects.equals(post.getUser().getId(), userId))
            throw new SecurityException("no Auth");
        postRepository.delete(post);
    }

    private PostResDTO assembleRes(Long viewerId, Posts post) {
        var dto = mapper.toRes(post);
        int likeCnt = postLikeRepository.countByPost_Id(post.getId());
        boolean liked = viewerId != null && postLikeRepository.existsByUser_IdAndPost_Id(viewerId, post.getId());
        dto.setLikeCount(likeCnt);
        dto.setIsLike(liked);

        return dto;
    }
}