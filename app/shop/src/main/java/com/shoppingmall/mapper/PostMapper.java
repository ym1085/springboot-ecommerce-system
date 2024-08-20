package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.PostSaveRequestDto;
import com.shoppingmall.dto.request.PostUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.vo.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<Post> getPosts(SearchRequestDto searchRequestDto);

    int count(SearchRequestDto searchRequestDto);

    Optional<Post> getPostByPostId(Integer postId);

    int savePost(PostSaveRequestDto postSaveRequestDto);

    int updatePost(PostUpdateRequestDto postUpdateRequestDto);

    int deletePostByPostId(Integer postId);

    int increasePostByPostId(Integer postId);

    String getCategoryNameByPostCategoryId(Integer categoryId);
}
