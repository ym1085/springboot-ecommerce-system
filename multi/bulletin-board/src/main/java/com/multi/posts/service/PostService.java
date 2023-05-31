package com.multi.posts.service;

import com.multi.posts.dto.request.PostRequestDto;
import com.multi.posts.dto.request.SearchRequestDto;
import com.multi.posts.dto.resposne.PagingResponseDto;
import com.multi.posts.dto.resposne.PostResponseDto;

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
