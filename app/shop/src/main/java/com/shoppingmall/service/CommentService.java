package com.shoppingmall.service;

import com.shoppingmall.vo.Comment;
import com.shoppingmall.dto.request.CommentRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.exception.FailDeleteCommentException;
import com.shoppingmall.exception.FailSaveCommentException;
import com.shoppingmall.exception.FailUpdateCommentException;
import com.shoppingmall.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentMapper commentMapper;

    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return this.getCommentsByPostId(postId);
    }

    @Transactional
    public List<CommentResponseDto> saveComment(CommentRequestDto commentRequestDto) {
        // 대댓글 등록 전 부모 댓글 존재 유무 판단, 일반 댓글은 해당 없음
        if (commentRequestDto.getParentId() != null) {
            if (isNotExistsCommentByCommentId(commentRequestDto.getParentId())) {
                return Collections.emptyList();
            }
        }

        Comment comment = new Comment(commentRequestDto);
        int responseCode = commentMapper.saveComment(comment);
        if (responseCode == 0) {
            throw new FailSaveCommentException();
        }

        List<CommentResponseDto> commentResponseDtos = getCommentsByPostId(commentRequestDto);
        return commentResponseDtos;
    }

    private boolean isNotExistsCommentByCommentId(Long parentId) {
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
    public List<CommentResponseDto> deleteComments(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        int responseCode = commentMapper.deleteComment(comment);
        if (responseCode == 0) {
            throw new FailDeleteCommentException();
        }

        List<CommentResponseDto> commentResponseDtos = getCommentsByPostId(commentRequestDto);
        return commentResponseDtos;
    }

    @Transactional
    public List<CommentResponseDto> deleteCommentsReply(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        int responseCode = commentMapper.deleteCommentReply(comment);
        if (responseCode == 0) {
            throw new FailDeleteCommentException();
        }

        List<CommentResponseDto> commentResponseDtos = getCommentsByPostId(commentRequestDto);
        return commentResponseDtos;
    }

    @Transactional
    public List<CommentResponseDto> updateCommentByCommentId(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        int responseCode = commentMapper.updateCommentByCommentId(comment);
        if (responseCode == 0) {
            throw new FailUpdateCommentException();
        }

        List<CommentResponseDto> commentResponseDtos = getCommentsByPostId(commentRequestDto);
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
