package com.webkit.travel_safety_backend.domain.service.Interface;

import com.webkit.travel_safety_backend.domain.model.dto.req.CommentReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.CommentResDTO;

import java.util.List;

//  - CommentService
//  createComment : 댓글 등록 , input( CommentReqDTO ) , output( response? List<CommentResDTO>? )
//  getCommentList : 댓글 조회 , input( postId ) , output( List<CommentResDTO> )
//  updateComment : 댓글 수정 , input( CommentReqDTO ) , output( response? List<CommentResDTO>? )
//  deleteComment : 댓글 삭제 , input( commentId ) , output( response? )
//  countComment : 댓글 집계 , input( postId ) , output( Integer count )
public interface CommentService {
    public CommentResDTO create(Long userId, CommentReqDTO reqDTO);
    public List<CommentResDTO> getList(Long postId);
    public CommentResDTO update(Long userId, Long commentId, CommentReqDTO reqDTO);
    public void delete(Long userId, Long commentId);
    public Integer count(Long postId);
}
