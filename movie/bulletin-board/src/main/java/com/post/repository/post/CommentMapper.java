package com.post.repository.post;

import com.post.domain.posts.Comment;
import com.post.dto.request.CommentRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<Comment> getComments(Long postId);

    int getCommentCountById(Long commentId);

    int saveComment(CommentRequestDto commentRequestDto);
}
