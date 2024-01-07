package com.shoppingmall.service;

import com.shoppingmall.vo.PostFilesVO;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    private final FileMapper fileMapper;

    public List<FileResponseDto> getFilesByPostId(Long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .map(FileResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public FileResponseDto getFileByPostFileId(Long postFileId) {
        PostFilesVO postFiles = fileMapper.getFileByPostFileId(postFileId).orElseGet(PostFilesVO::new);
        return FileResponseDto.toDto(postFiles);
    }

    @Transactional
    public void increaseDownloadCntByFileId(Long fileId) {
        fileMapper.increaseDownloadCntByFileId(fileId);
    }
}
