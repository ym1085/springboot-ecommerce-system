package com.shoppingmall.mapper;

import com.shoppingmall.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<CommentVO> getComments(Long postId);

    int getCommentCountByCommentId(Long commentId);

    int saveComment(CommentVO comment);

    int deleteComment(CommentVO comment);

    int deleteCommentReply(CommentVO comment);

    int getCommentReplyCountByCommentId(Long commentId);

    int updateCommentByCommentId(CommentVO comment);
}
