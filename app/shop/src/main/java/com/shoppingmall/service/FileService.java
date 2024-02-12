package com.shoppingmall.service;

import com.shoppingmall.vo.PostFiles;
import com.shoppingmall.dto.response.FileResponseDto;
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

    public List<FileResponseDto> getFilesByPostId(Long postId) {
        return postFileMapper.getFilesByPostId(postId)
                .stream()
                .map(FileResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public FileResponseDto getFileByPostFileId(Long postFileId) {
        PostFiles postFiles = postFileMapper.getFileByPostFileId(postFileId).orElseGet(PostFiles::new);
        return FileResponseDto.toDto(postFiles);
    }

    @Transactional
    public void increaseDownloadCntByFileId(Long fileId) {
        postFileMapper.increaseDownloadCntByFileId(fileId);
    }
}
