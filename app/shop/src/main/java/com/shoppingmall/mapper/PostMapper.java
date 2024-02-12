package com.shoppingmall.mapper;

import com.shoppingmall.vo.Post;
import com.shoppingmall.dto.request.SearchRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<Post> getPosts(SearchRequestDto searchRequestDto);

    int count(SearchRequestDto searchRequestDto);

    Optional<Post> getPostByPostId(Long postId);

    int savePost(Post post);

    int updatePost(Post post);

    int deletePostByPostId(Long postId);

    int increasePostByPostId(Long postId);
}
