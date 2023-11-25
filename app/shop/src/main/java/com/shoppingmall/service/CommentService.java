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
    public List<CommentResponseDto> getComments(Long postId) {
        return commentMapper.getComments(postId)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
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
            commentResponseDtos = commentMapper.getComments(commentRequestDto.getPostId())
                    .stream()
                    .filter(Objects::nonNull)
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }
        return commentResponseDtos;
    }

    private boolean isExistsCommentsCountByParentId(Long parentId) {
        return commentMapper.getCommentCountByCommentId(parentId) > 0;
    }

    @Transactional
    public MessageCode deleteCommentById(CommentRequestDto commentRequestDto) {
        if (!commentRequestDto.hasBothIds()) {
            log.error("commentId, parentId not exist!");
            return MessageCode.FAIL_DELETE_COMMENT;
        }

        MessageCode messageCode = null;
        Comment comment = new Comment(commentRequestDto);

        if (commentRequestDto.hasBothIds()) { // commentId + parentId ==> null 이 아니면 단일, 대댓글 전부 삭제
            messageCode = (commentMapper.deleteCommentByCommentIdAndParentId(comment) > 0)
                    ? MessageCode.SUCCESS_DELETE_COMMENT
                    : MessageCode.FAIL_DELETE_COMMENT;
        } else {
            if (getChildCommentCount(comment.getCommentId())) { // 자식 댓글이 있는 경우 삭제를 막기 위해 아래 로직 구현
                log.error("comment number where the child reply exists, error, commentId = {}", comment.getCommentId());
                return MessageCode.FAIL_DELETE_COMMENT;
            }
            messageCode = (commentMapper.deleteCommentByCommentId(comment) > 0)
                    ? MessageCode.SUCCESS_DELETE_COMMENT
                    : MessageCode.FAIL_DELETE_COMMENT;
        }
        return messageCode;
    }

    private boolean getChildCommentCount(Long commentId) {
        return commentMapper.getChildCommentCountById(commentId) > 0;
    }

    @Transactional
    public List<CommentResponseDto> updateCommentByCommentId(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        MessageCode messageCode = (commentMapper.updateCommentByCommentId(comment) > 0) ? MessageCode.SUCCESS_UPDATE_COMMENT : MessageCode.FAIL_UPDATE_COMMENT;

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        if (messageCode == MessageCode.SUCCESS_UPDATE_COMMENT) {
            commentResponseDtos = commentMapper.getComments(commentRequestDto.getPostId())
                    .stream()
                    .filter(Objects::nonNull)
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }
        return commentResponseDtos;
    }
}
