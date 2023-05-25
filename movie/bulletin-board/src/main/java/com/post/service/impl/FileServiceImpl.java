package com.post.service.impl;

import com.post.constant.ResponseCode;
import com.post.domain.posts.Files;
import com.post.repository.post.FileMapper;
import com.post.service.FileService;
import com.post.web.dto.request.FileRequestDto;
import com.post.web.dto.resposne.FileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Transactional
    @Override
    public int saveFiles(Long postId, List<FileRequestDto> files) {
        if (CollectionUtils.isEmpty(files) || postId == null) {
            return ResponseCode.FAIL.getResponseCode();
        }
        for (FileRequestDto file : files) {
            file.setPostId(postId);
        }
        return fileMapper.saveFiles(files);
    }

    @Override
    public List<FileResponseDto> getFiles(Long postId) {
        return fileMapper.getFiles(postId).stream()
                .filter(Objects::nonNull)
                .map(file -> new FileResponseDto(file))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public FileResponseDto getFileById(Long fileId) {
        Files files = fileMapper.getFileById(fileId).orElseGet(() -> new Files());
        FileResponseDto fileResponseDto = new FileResponseDto(files);
        return fileResponseDto;
    }

    @Override
    public void increaseDownloadCnt(Long fileId) {
        fileMapper.increaseDownloadCnt(fileId);
    }
}
