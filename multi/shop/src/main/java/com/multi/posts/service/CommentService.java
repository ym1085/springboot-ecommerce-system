package com.multi.posts.service;

import com.multi.posts.dto.request.CommentRequestDto;
import com.multi.posts.dto.resposne.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getComments(Long postId);

    int saveComment(CommentRequestDto commentRequestDto);

    int deleteCommentById(CommentRequestDto commentRequestDto);

    int updateCommentById(CommentRequestDto commentRequestDto);
}
