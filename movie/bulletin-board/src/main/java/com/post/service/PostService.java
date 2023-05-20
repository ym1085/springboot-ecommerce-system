package com.post.service;

import com.post.web.dto.resposne.PostResponseDto;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts();

    PostResponseDto getPostById(Long postId);
}
