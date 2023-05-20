package com.post.service.impl;

import com.post.domain.posts.Post;
import com.post.repository.post.PostMapper;
import com.post.service.PostService;
import com.post.web.dto.request.PostRequestSaveDto;
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

    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> getPosts() {
        return postMapper.getPosts().stream()
                .filter(Objects::nonNull)
                .map(post -> new PostResponseDto(post))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponseDto getPostById(Long postId) {
        return postMapper.getPostById(postId)
                .map(post -> new PostResponseDto(post))
                .orElse(new PostResponseDto());
    }

    @Override
    public Long savePost(PostRequestSaveDto postRequestSaveDto) {
        Post post = new Post(postRequestSaveDto);
        postMapper.savePost(post);
        return post.getPostId();
    }
}
