package com.post.repository.post;

import com.post.domain.posts.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<Post> getPostList();

    Optional<Post> getPostById(Long postId);

}
