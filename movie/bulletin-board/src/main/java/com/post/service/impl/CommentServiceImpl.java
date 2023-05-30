package com.post.service.impl;

import com.post.constant.ResponseCode;
import com.post.dto.request.CommentRequestDto;
import com.post.dto.resposne.CommentResponseDto;
import com.post.repository.post.CommentMapper;
import com.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentResponseDto> getComments(Long postId) {
        return commentMapper.getComments(postId)
                .stream()
                .filter(Objects::nonNull)
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public int saveComment(CommentRequestDto commentRequestDto) {
        // 대댓글을 등록하는 경우 부모 댓글(commentId)가 존재하는지 먼저 확인 후 대댓글 등록을 진행 한다
        if (commentRequestDto.getParentId() != null && !isExistsCommentId(commentRequestDto.getParentId())) {
            return ResponseCode.FAIL.getResponseCode();
        }

        return commentMapper.saveComment(commentRequestDto); // 일반 댓글 등록
    }

    private boolean isExistsCommentId(Long parentId) {
        return commentMapper.getCommentCountById(parentId) > 0;
    }
}
