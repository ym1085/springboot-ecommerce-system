package com.shoppingmall.service;

import com.shoppingmall.dto.request.CommentDeleteRequestDto;
import com.shoppingmall.dto.request.CommentSaveRequestDto;
import com.shoppingmall.dto.request.CommentUpdateRequestDto;
import com.shoppingmall.exception.CommentException;
import com.shoppingmall.mapper.CommentMapper;
import com.shoppingmall.vo.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.shoppingmall.common.code.failure.post.PostFailureCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentMapper commentMapper;

    @Transactional
    public List<Comment> saveComment(CommentSaveRequestDto commentSaveRequestDto) {
        // 대댓글 등록 전 부모 댓글 존재 유무 판단, 일반 댓글은 해당 없음
        if (commentSaveRequestDto.getParentId() != null) {
            if (isNotExistsCommentByCommentId(commentSaveRequestDto.getParentId())) {
                return Collections.emptyList();
            }
        }
        // TODO: 댓글 저장하는 로직 추후 확인 필요
        if (commentMapper.saveComment(commentSaveRequestDto) < 1) {
            log.error(FAIL_SAVE_COMMENT.getMessage());
            throw new CommentException(FAIL_SAVE_COMMENT);
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
    public List<Comment> deleteComments(CommentDeleteRequestDto commentDeleteRequestDto) {
        if (commentMapper.deleteComment(commentDeleteRequestDto) < 1) {
            log.error(FAIL_DELETE_COMMENT.getMessage());
            throw new CommentException(FAIL_DELETE_COMMENT);
        }
        return getCommentsByPostId(commentDeleteRequestDto.getPostId());
    }

    @Transactional
    public List<Comment> deleteCommentsReply(CommentDeleteRequestDto commentDeleteRequestDto) {
        if (commentMapper.deleteCommentReply(commentDeleteRequestDto) < 1) {
            log.error(FAIL_DELETE_COMMENT.getMessage());
            throw new CommentException(FAIL_DELETE_COMMENT);
        }
        return getCommentsByPostId(commentDeleteRequestDto.getPostId());
    }

    @Transactional
    public List<Comment> updateCommentByCommentId(CommentUpdateRequestDto commentUpdateRequestDto) {
        if (commentMapper.updateCommentByCommentId(commentUpdateRequestDto) < 1) {
            log.error(FAIL_UPDATE_COMMENT.getMessage());
            throw new CommentException(FAIL_UPDATE_COMMENT);
        }
        return getCommentsByPostId(commentUpdateRequestDto.getPostId());
    }

    private List<Comment> getCommentsByPostId(Integer postId) {
        if (postId == null) {
            return Collections.emptyList();
        }
        return commentMapper.getComments(postId);
    }
}
