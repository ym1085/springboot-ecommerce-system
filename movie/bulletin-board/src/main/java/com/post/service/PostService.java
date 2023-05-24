package com.post.service;

import com.post.web.dto.request.PostRequestDto;
import com.post.web.dto.resposne.PostResponseDto;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts();

    PostResponseDto getPostById(Long postId);

    Long savePost(PostRequestDto postRequestDto);

    int uploadPost(PostRequestDto postRequestDto);

    int deletePost(long postId);
}
