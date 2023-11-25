package com.shoppingmall.repository;

import com.shoppingmall.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<Comment> getComments(Long postId);

    int getCommentCountByCommentId(Long commentId);

    int saveComment(Comment comment);

    int deleteCommentByCommentIdAndParentId(Comment comment);

    int deleteCommentByCommentId(Comment comment);

    int getChildCommentCountById(Long commentId);

    int updateCommentByCommentId(Comment comment);

}
