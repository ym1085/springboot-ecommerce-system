package com.multi.posts.service;

import com.multi.common.utils.message.MessageCode;
import com.multi.posts.domain.Comment;
import com.multi.posts.dto.request.CommentRequestDto;
import com.multi.posts.dto.resposne.CommentResponseDto;
import com.multi.posts.repository.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveComment(CommentRequestDto commentRequestDto) {
        // 대댓글을 등록하는 경우 부모 댓글(commentId)가 존재하는지 먼저 확인 후 대댓글 등록을 진행 한다
        if (commentRequestDto.getParentId() != null && !isExistsCommentId(commentRequestDto.getParentId())) {
            log.error("parent comment does not exist at the time of the reply, parentId = {}", commentRequestDto.getParentId());
            return MessageCode.FAIL.getCode();
        }
        Comment comment = new Comment(commentRequestDto);
        return commentMapper.saveComment(comment); // 일반 댓글 등록
    }

    private boolean isExistsCommentId(Long parentId) {
        return commentMapper.getCommentCountById(parentId) > 0;
    }

    @Transactional
    public int deleteCommentById(CommentRequestDto commentRequestDto) {
        if (commentRequestDto.getCommentId() == null && commentRequestDto.getParentId() == null) {
            log.error("comment ID and parent comment ID do not exist error!");
            return MessageCode.FAIL.getCode();
        }

        int result = 0;
        Comment comment = new Comment(commentRequestDto);
        if (commentRequestDto.getCommentId() != null && commentRequestDto.getParentId() != null) {
            result = commentMapper.deleteCommentByCommentIdAndParentId(comment); // 댓글 + 대댓글 전부 삭제
        } else {
            Long commentId = comment.getCommentId();
            boolean isExistsChildCount = getChildCommentCount(commentId);
            if (isExistsChildCount) { // 자식 댓글이 있는 경우 삭제 하면 안됨
                log.error("comment number where the child reply exists, error, commentId = {}", commentId);
                return MessageCode.FAIL.getCode();
            }
            result = commentMapper.deleteCommentByCommentId(comment); // 대댓글, 단일 댓글 삭제
        }
        return result;
    }

    private boolean getChildCommentCount(Long commentId) {
        return commentMapper.getChildCommentCountById(commentId) > 0;
    }

    @Transactional
    public int updateCommentById(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        return commentMapper.updateCommentById(comment);
    }
}
