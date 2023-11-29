package com.shoppingmall.service;

import com.shoppingmall.common.MessageCode;
import com.shoppingmall.domain.Comment;
import com.shoppingmall.dto.request.CommentRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.repository.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return this.getCommentsByPostId(postId);
    }

    @Transactional
    public List<CommentResponseDto> saveComment(CommentRequestDto commentRequestDto) {
        // 대댓글 등록 전 부모 댓글 존재 유무 판단, 일반 댓글은 해당 없음
        if (commentRequestDto.getParentId() != null) {
            if (!isExistsCommentsCountByParentId(commentRequestDto.getParentId())) {
                // https://velog.io/@ychxexn/Collections.emptyList-vs-new-ArrayList
                log.error("commentId, parentId not exist!, commentId = {}, parentId = {}", commentRequestDto.getCommentId(), commentRequestDto.getParentId());
                return Collections.emptyList();
            }
        }

        Comment comment = new Comment(commentRequestDto);
        MessageCode messageCode = (commentMapper.saveComment(comment) > 0) ? MessageCode.SUCCESS_SAVE_COMMENT : MessageCode.FAIL_SAVE_COMMENT;

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        if (messageCode == MessageCode.SUCCESS_SAVE_COMMENT) {
            commentResponseDtos = this.getCommentsByPostId(commentRequestDto);
        }
        return commentResponseDtos;
    }

    private boolean isExistsCommentsCountByParentId(Long parentId) {
        return commentMapper.getCommentCountByCommentId(parentId) > 0;
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
    public List<CommentResponseDto> deleteComments(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        MessageCode messageCode = commentMapper.deleteComment(comment) > 0 ? MessageCode.SUCCESS_DELETE_COMMENT : MessageCode.FAIL_DELETE_COMMENT; // 댓글 + 대댓글 삭제

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        if (messageCode == MessageCode.SUCCESS_DELETE_COMMENT) {
            commentResponseDtos = this.getCommentsByPostId(commentRequestDto);
        }

        return commentResponseDtos;
    }

    @Transactional
    public List<CommentResponseDto> deleteCommentsReply(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        MessageCode messageCode = commentMapper.deleteCommentReply(comment) > 0 ? MessageCode.SUCCESS_DELETE_COMMENT : MessageCode.FAIL_DELETE_COMMENT; // 대댓글 삭제

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        if (messageCode == MessageCode.SUCCESS_DELETE_COMMENT) {
            commentResponseDtos = this.getCommentsByPostId(commentRequestDto);
        }

        return commentResponseDtos;
    }

    @Transactional
    public List<CommentResponseDto> updateCommentByCommentId(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        MessageCode messageCode = commentMapper.updateCommentByCommentId(comment) > 0 ? MessageCode.SUCCESS_UPDATE_COMMENT : MessageCode.FAIL_UPDATE_COMMENT;

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        if (messageCode == MessageCode.SUCCESS_UPDATE_COMMENT) {
            commentResponseDtos = this.getCommentsByPostId(commentRequestDto);
        }
        return commentResponseDtos;
    }

    private List<CommentResponseDto> getCommentsByPostId(CommentRequestDto commentRequestDto) {
        return commentMapper.getComments(commentRequestDto.getPostId())
                .stream()
                .filter(Objects::nonNull)
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
