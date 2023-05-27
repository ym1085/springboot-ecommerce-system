package com.post.service;

import com.post.web.dto.request.PostRequestDto;
import com.post.web.dto.request.SearchRequestDto;
import com.post.web.dto.resposne.PagingResponseDto;
import com.post.web.dto.resposne.PostResponseDto;

public interface PostService {

    PagingResponseDto<PostResponseDto> getPosts(SearchRequestDto searchRequestDto);

    PostResponseDto getPostById(Long postId);

    Long savePost(PostRequestDto postRequestDto);

    int uploadPost(PostRequestDto postRequestDto);

    int deletePost(long postId);
}
