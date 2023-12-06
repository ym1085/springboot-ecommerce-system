package com.shoppingmall.mapper;

import com.shoppingmall.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<Comment> getComments(Long postId);

    int getCommentCountByCommentId(Long commentId);

    int saveComment(Comment comment);

    int deleteComment(Comment comment);

    int deleteCommentReply(Comment comment);

    int getCommentReplyCountByCommentId(Long commentId);

    int updateCommentByCommentId(Comment comment);
}
