package com.multi.posts.service;

import com.multi.posts.domain.Files;
import com.multi.posts.dto.request.FileRequestDto;
import com.multi.posts.dto.resposne.FileResponseDto;
import com.multi.posts.repository.FileMapper;
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
    public List<FileResponseDto> getFiles(Long postId) {
        return fileMapper.getFiles(postId).stream()
                .map(file -> new FileResponseDto(file))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileResponseDto getFileById(Long fileId) {
        Files files = fileMapper.getFileById(fileId).orElseGet(Files::new);
        return new FileResponseDto(files);
    }

    @Transactional
    public void increaseDownloadCnt(Long fileId) {
        fileMapper.increaseDownloadCnt(fileId);
    }
}
