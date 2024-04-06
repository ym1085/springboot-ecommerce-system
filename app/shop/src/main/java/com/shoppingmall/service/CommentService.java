package com.shoppingmall.service;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.dto.request.CommentDeleteRequestDto;
import com.shoppingmall.dto.request.CommentSaveRequestDto;
import com.shoppingmall.dto.request.CommentUpdateRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.exception.FailDeleteCommentException;
import com.shoppingmall.exception.FailSaveCommentException;
import com.shoppingmall.exception.FailUpdateCommentException;
import com.shoppingmall.mapper.CommentMapper;
import com.shoppingmall.vo.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentMapper commentMapper;

    @Transactional
    public List<CommentResponseDto> saveComment(CommentSaveRequestDto commentSaveRequestDto) {
        // 대댓글 등록 전 부모 댓글 존재 유무 판단, 일반 댓글은 해당 없음
        if (commentSaveRequestDto.getParentId() != null) {
            if (isNotExistsCommentByCommentId(commentSaveRequestDto.getParentId())) {
                return Collections.emptyList();
            }
        }

        Comment comment = new Comment();
        if (commentMapper.saveComment(comment) < 1) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.SAVE_COMMENT.getMessage());
            throw new FailSaveCommentException(ErrorCode.SAVE_COMMENT);
        }

        return getCommentsByPostId(commentSaveRequestDto.getPostId());
    }

    private boolean isNotExistsCommentByCommentId(Integer parentId) {
        return commentMapper.getCommentCountByCommentId(parentId) <= 0;
    }

    /**
     * 부모 댓글, 대댓글 삭제
     *
     * commentId, parentId 둘다 파라미터로 들어오는 경우는 '부모 댓글 + 대댓글 삭제',
     * commentId만 파라미터로 들어오면 '대댓글 삭제'
     *
     * 중복 로직 수정은 고민 해봅시다
     */
    @Transactional
    public List<CommentResponseDto> deleteComments(CommentDeleteRequestDto commentDeleteRequestDto) {
        if (commentMapper.deleteComment(commentDeleteRequestDto.toEntity()) < 1) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.DELETE_COMMENT.getMessage());
            throw new FailDeleteCommentException(ErrorCode.DELETE_COMMENT);
        }

        return getCommentsByPostId(commentDeleteRequestDto.getPostId());
    }

    @Transactional
    public List<CommentResponseDto> deleteCommentsReply(CommentDeleteRequestDto commentDeleteRequestDto) {
        if (commentMapper.deleteCommentReply(commentDeleteRequestDto.toEntity()) < 1) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.DELETE_COMMENT.getMessage());
            throw new FailDeleteCommentException(ErrorCode.DELETE_COMMENT);
        }

        return getCommentsByPostId(commentDeleteRequestDto.getPostId());
    }

    @Transactional
    public List<CommentResponseDto> updateCommentByCommentId(CommentUpdateRequestDto commentUpdateRequestDto) {
        if (commentMapper.updateCommentByCommentId(commentUpdateRequestDto.toEntity()) < 1) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.UPDATE_COMMENT.getMessage());
            throw new FailUpdateCommentException(ErrorCode.UPDATE_COMMENT);
        }

        return getCommentsByPostId(commentUpdateRequestDto.getPostId());
    }

    private List<CommentResponseDto> getCommentsByPostId(Integer postId) {
        if (postId == null) {
            return Collections.emptyList();
        }

        return commentMapper.getComments(postId)
                .stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
