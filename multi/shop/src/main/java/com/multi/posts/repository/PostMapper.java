package com.multi.posts.repository;

import com.multi.posts.domain.Post;
import com.multi.posts.dto.request.SearchRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<Post> getPosts(SearchRequestDto searchRequestDto);

    int count(SearchRequestDto searchRequestDto);

    Optional<Post> getPostById(Long postId);

    int savePost(Post post);

    Long updatePostById(Post post);

    Long deletePostById(long postId);

}
