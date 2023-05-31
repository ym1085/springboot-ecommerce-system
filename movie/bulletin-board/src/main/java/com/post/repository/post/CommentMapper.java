package com.post.repository.post;

import com.post.domain.posts.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<Comment> getComments(Long postId);

    int getCommentCountById(Long commentId);

    int saveComment(Comment comment);

    int deleteCommentByCommentIdAndParentId(Comment comment);

    int deleteCommentByCommentId(Comment comment);

    int getChildCommentCountById(Long commentId);

    int updateCommentById(Comment comment);

}
