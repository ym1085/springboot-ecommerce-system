package com.post.service.impl;

import com.post.constant.ResponseCode;
import com.post.dto.request.CommentRequestDto;
import com.post.repository.post.CommentMapper;
import com.post.service.CommentService;
import com.post.dto.resposne.CommentResponseDto;
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
        // commentId가 존재하는지 한번 확인?
        if (!isExistsCommentId(commentRequestDto.getParentId())) {
            return ResponseCode.FAIL.getResponseCode();
        }
        return commentMapper.saveComment(commentRequestDto);
    }

    private boolean isExistsCommentId(Long parentId) {
        // commentId가 없을 경우는 절대 없겠지만, 혹시 없는 경우 INSERT가 되어버리면 기존 DB 데이터가
        // 꼬일 수 있기 때문에 한번 검증 차원에서 확인 후 넣는 걸로 진행
        return commentMapper.getCommentCountById(parentId) > 0;
    }
}
