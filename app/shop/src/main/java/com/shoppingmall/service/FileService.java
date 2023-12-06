package com.shoppingmall.service;

import com.shoppingmall.domain.PostFiles;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileMapper fileMapper;

    @Transactional(readOnly = true)
    public List<FileResponseDto> getFilesByPostId(Long postId) {
        return fileMapper.getFilesByPostId(postId).stream()
                .map(file -> new FileResponseDto(file))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileResponseDto getFileByPostFileId(Long postFileId) {
        PostFiles postFiles = fileMapper.getFileByPostFileId(postFileId).orElseGet(PostFiles::new);
        return new FileResponseDto(postFiles);
    }

    @Transactional
    public void increaseDownloadCntByFileId(Long fileId) {
        fileMapper.increaseDownloadCntByFileId(fileId);
    }
}
