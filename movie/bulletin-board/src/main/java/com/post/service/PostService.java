package com.post.service;

import com.post.dto.request.PostRequestDto;
import com.post.dto.request.SearchRequestDto;
import com.post.dto.resposne.PagingResponseDto;
import com.post.dto.resposne.PostResponseDto;

public interface PostService {

    PagingResponseDto<PostResponseDto> getPosts(SearchRequestDto searchRequestDto);

    PostResponseDto getPostById(Long postId);

    Long savePost(PostRequestDto postRequestDto);

    int updatePost(PostRequestDto postRequestDto);

    int deletePost(long postId);
}
