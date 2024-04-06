package com.shoppingmall.service;

import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.dto.response.PostFileResponseDto;
import com.shoppingmall.mapper.PostFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    private final PostFileMapper postFileMapper;

    public List<FileResponseDto> getFilesByPostId(Integer postId) {
        return postFileMapper.getFilesByPostId(postId)
                .stream()
                .map(PostFileResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public PostFileResponseDto getFileByPostFileId(Integer postFileId) {
        return postFileMapper.getFileByPostFileId(postFileId)
                .map(PostFileResponseDto::toDto)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
    }

    @Transactional
    public void increaseDownloadCntByFileId(Integer fileId) {
        postFileMapper.increaseDownloadCntByFileId(fileId);
    }
}
