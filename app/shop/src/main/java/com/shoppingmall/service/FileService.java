package com.shoppingmall.service;

import com.shoppingmall.domain.PostFiles;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.repository.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileMapper fileMapper;

    @Transactional
    public void saveFiles(Long postId, List<FileRequestDto> files) {
        if (CollectionUtils.isEmpty(files) || postId == null) {
            return;
        }
        for (FileRequestDto file : files) {
            file.setPostId(postId);
        }
        fileMapper.saveFiles(files);
    }

    @Transactional(readOnly = true)
    public List<FileResponseDto> getFilesByPostId(Long postId) {
        return fileMapper.getFilesByPostId(postId).stream()
                .map(file -> new FileResponseDto(file))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileResponseDto getFileByFileId(Long fileId) {
        PostFiles postFiles = fileMapper.getFileByFileId(fileId).orElseGet(PostFiles::new);
        return new FileResponseDto(postFiles);
    }

    @Transactional
    public void increaseDownloadCntByFileId(Long fileId) {
        fileMapper.increaseDownloadCntByFileId(fileId);
    }
}
