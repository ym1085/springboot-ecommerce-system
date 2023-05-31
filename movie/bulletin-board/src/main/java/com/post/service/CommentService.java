package com.post.service;

import com.post.dto.request.CommentRequestDto;
import com.post.dto.resposne.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getComments(Long postId);

    int saveComment(CommentRequestDto commentRequestDto);

    int deleteCommentById(CommentRequestDto commentRequestDto);

    int updateCommentById(CommentRequestDto commentRequestDto);
}
