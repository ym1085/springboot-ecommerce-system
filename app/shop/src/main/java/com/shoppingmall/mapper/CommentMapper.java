package com.shoppingmall.mapper;

import com.shoppingmall.vo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<Comment> getComments(Integer postId);

    int getCommentCountByCommentId(Integer commentId);

    int saveComment(Comment comment);

    int deleteComment(Comment comment);

    int deleteCommentReply(Comment comment);

    int getCommentReplyCountByCommentId(Integer commentId);

    int updateCommentByCommentId(Comment comment);
}
