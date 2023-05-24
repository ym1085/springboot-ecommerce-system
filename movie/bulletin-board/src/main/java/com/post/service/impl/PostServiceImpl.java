package com.post.service.impl;

import com.post.constant.ResponseCode;
import com.post.domain.posts.Post;
import com.post.repository.post.FileMapper;
import com.post.repository.post.PostMapper;
import com.post.service.PostService;
import com.post.utils.FileUtils;
import com.post.web.dto.request.FileRequestDto;
import com.post.web.dto.request.PostRequestDto;
import com.post.web.dto.resposne.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final FileUtils fileUtils;

    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> getPosts() {
        return postMapper.getPosts()
                .stream()
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
    public Long savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postMapper.savePost(post);
        return post.getPostId();
    }

    @Override
    public int uploadPost(PostRequestDto postRequestDto) {
        Long successId = postMapper.updatePostById(new Post(postRequestDto));
        if (successId == null || successId == 0) {
            return ResponseCode.FAIL.getResponseCode();
        }

        List<MultipartFile> files = postRequestDto.getFiles();
        return updateFilesById(postRequestDto.getPostId(), files); // 파일이 존재하는 경우 해당 게시글에 매핑 된 파일 정보를 일단 전부 삭제 후 업데이트
    }

    private int updateFilesById(Long postId, List<MultipartFile> files) {
        if (!isFileSizeOverThanZero(files)) {
            return ResponseCode.FAIL.getResponseCode();
        }

        int successId = fileMapper.deleteFilesById(postId);
        if (successId == 0) {
            return ResponseCode.FAIL.getResponseCode();
        } else if (successId > 0) {
            List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(files);
            setFileInfoPostId(postId, fileRequestDtos);
            fileMapper.saveFiles(fileRequestDtos); // 클라이언트가 요청 한 파일 신규 저장
        }
        return ResponseCode.SUCCESS.getResponseCode();
    }

    private boolean isFileSizeOverThanZero(List<MultipartFile> files) {
        return (files.size() > 0);
    }

    private void setFileInfoPostId(Long postId, List<FileRequestDto> fileRequestDtos) {
        fileRequestDtos.stream()
                .filter(Objects::nonNull)
                .forEach(fileRequestDto -> fileRequestDto.setPostId(postId));
    }
}
