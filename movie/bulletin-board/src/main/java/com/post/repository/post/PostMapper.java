package com.post.repository.post;

import com.post.domain.posts.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    List<Post> getPosts();

    Optional<Post> getPostById(Long postId);

    int savePost(Post post);

    Long updatePostById(Post post);

    Long deletePostById(long postId);

}
