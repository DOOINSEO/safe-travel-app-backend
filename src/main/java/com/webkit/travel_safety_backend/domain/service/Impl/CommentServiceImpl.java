package com.webkit.travel_safety_backend.domain.service.Impl;

import com.webkit.travel_safety_backend.domain.model.dto.req.CommentReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.CommentResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.Comments;
import com.webkit.travel_safety_backend.domain.model.entity.Posts;
import com.webkit.travel_safety_backend.domain.model.entity.Users;
import com.webkit.travel_safety_backend.domain.model.mapper.CommentMapper;
import com.webkit.travel_safety_backend.domain.repository.CommentRepository;
import com.webkit.travel_safety_backend.domain.repository.PostRepository;
import com.webkit.travel_safety_backend.domain.repository.UserRepository;
import com.webkit.travel_safety_backend.domain.service.Interface.CommentService;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper mapper;

    @Override
    public CommentResDTO create(Long userId, CommentReqDTO reqDTO) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user"));
        Posts post = postRepository.findById(reqDTO.getPostId()).orElseThrow(()->new EntityNotFoundException("post"));
        Comments comment = Comments.builder().post(post).user(user).content(reqDTO.getContent()).build();
        commentRepository.save(comment);

        return mapper.toRes(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResDTO> getList(Long postId) {
        return commentRepository.findByPost_IdOrderByCreatedAtAsc(postId).stream().map(mapper::toRes).toList();
    }

    @Override
    public CommentResDTO update(Long userId, Long commentId, CommentReqDTO reqDTO) {
        Comments comment = commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException("comment"));

        if(!Objects.equals(comment.getUser().getId(), userId))
            throw new SecurityException("권한이 없습니다.");

        comment.setContent(reqDTO.getContent());
        commentRepository.save(comment);
        return mapper.toRes(comment);
    }

    @Override
    public void delete(Long userId, Long commentId) {
        Comments comment = commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException("comment"));

        if(!Objects.equals(comment.getUser().getId(), userId))
            throw new SecurityException("권한이 없습니다.");
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer count(Long postId) {
        return commentRepository.countByPost_Id(postId);
    }
}
