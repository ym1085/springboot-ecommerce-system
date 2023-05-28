package com.post.service;

import com.post.dto.resposne.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getComments(Long postId);

}
