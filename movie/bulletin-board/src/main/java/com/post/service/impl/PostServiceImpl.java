package com.post.service.impl;

import com.post.repository.post.PostMapper;
import com.post.service.PostService;
import com.post.web.dto.resposne.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper mapper;

    /**
     * @return 게시글 리스트 정보 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> getPosts() {
        return mapper.getPosts().stream()
                .filter(Objects::nonNull)
                .map(post -> new PostResponseDto(post))
                .collect(Collectors.toList());
    }

    /**
     * @param postId 게시글 번호
     * @return 단일 게시글 정보 반환
     */
    @Transactional(readOnly = true)
    @Override
    public PostResponseDto getPostById(Long postId) {
        return mapper.getPostById(postId)
                .map(post -> new PostResponseDto(post))
                .orElse(new PostResponseDto());
    }
}
