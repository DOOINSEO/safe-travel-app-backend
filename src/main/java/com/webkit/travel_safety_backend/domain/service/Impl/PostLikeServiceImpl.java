package com.webkit.travel_safety_backend.domain.service.Impl;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostLikeReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostLikeResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.PostLike;
import com.webkit.travel_safety_backend.domain.model.entity.Posts;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.PostLikeMapper;
import com.webkit.travel_safety_backend.domain.repository.PostLikeRepository;
import com.webkit.travel_safety_backend.domain.repository.PostRepository;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import com.webkit.travel_safety_backend.domain.service.Interface.PostLikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeMapper mapper;

    @Override
    public PostLikeResDTO create(Long userId, PostLikeReqDTO postLikeReqDTO) {
        Long postId = postLikeReqDTO.getPostId();
        if(postLikeRepository.existsByUser_IdAndPost_Id(userId, postId)){
            return get(userId, postId);
        }
        Users user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("user"));
        Posts post = postRepository.findById(postId).orElseThrow(()->new EntityNotFoundException("post"));

        postLikeRepository.save(PostLike.builder().user(user).post(post).build());
        return get(userId, postId);
    }

    @Override
    @Transactional(readOnly = true)
    public PostLikeResDTO get(Long userId, Long postId) {
        boolean liked = postLikeRepository.existsByUser_IdAndPost_Id(userId, postId);
        return PostLikeResDTO.builder().postId(postId).userId(userId).isLike(liked).build();
    }

    @Override
    public PostLikeResDTO delete(Long userId, Long postId) {
        postLikeRepository.deleteByUser_IdAndPost_Id(userId, postId);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer count(Long postId) {
        return postLikeRepository.countByPost_Id(postId);
    }
}
