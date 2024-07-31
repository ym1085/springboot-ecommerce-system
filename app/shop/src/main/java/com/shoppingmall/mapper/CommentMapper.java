package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.CommentDeleteRequestDto;
import com.shoppingmall.dto.request.CommentSaveRequestDto;
import com.shoppingmall.dto.request.CommentUpdateRequestDto;
import com.shoppingmall.vo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /* 계층형 댓글 리스트 조회 */
    List<Comment> getComments(Integer postId);

    int getCommentCountByCommentId(Integer commentId);

    int saveComment(CommentSaveRequestDto comment);

    int deleteComment(CommentDeleteRequestDto comment);

    int deleteCommentReply(CommentDeleteRequestDto comment);

    int getCommentReplyCountByCommentId(Integer commentId);

    int updateCommentByCommentId(CommentUpdateRequestDto comment);
}
