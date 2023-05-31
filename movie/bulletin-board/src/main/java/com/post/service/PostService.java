package com.post.service;

import com.post.dto.request.PostRequestDto;
import com.post.dto.request.SearchRequestDto;
import com.post.dto.resposne.PagingResponseDto;
import com.post.dto.resposne.PostResponseDto;

/**
 * OCP(개방 폐쇄 원칙)을 준수 할 수 있는 방법..?
 *
 * PostApiController -> PostService <- PostServiceImpl
 */
public interface PostService {

    PagingResponseDto<PostResponseDto> getPosts(SearchRequestDto searchRequestDto);

    PostResponseDto getPostById(Long postId);

    Long savePost(PostRequestDto postRequestDto);

    int updatePost(PostRequestDto postRequestDto);

    int deletePost(long postId);
}
