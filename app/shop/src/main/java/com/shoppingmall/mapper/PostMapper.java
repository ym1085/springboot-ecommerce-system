package com.shoppingmall.mapper;

import com.shoppingmall.vo.PostVO;
import com.shoppingmall.dto.request.SearchRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<PostVO> getPosts(SearchRequestDto searchRequestDto);

    int count(SearchRequestDto searchRequestDto);

    Optional<PostVO> getPostByPostId(Long postId);

    int savePost(PostVO post);

    int updatePost(PostVO post);

    int deletePostByPostId(Long postId);

    int increasePostByPostId(Long postId);
}
