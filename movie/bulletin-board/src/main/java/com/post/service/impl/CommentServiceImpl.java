package com.post.service.impl;

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
}
