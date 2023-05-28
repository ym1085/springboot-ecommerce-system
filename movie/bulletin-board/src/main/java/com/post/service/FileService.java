package com.post.service;

import com.post.dto.request.FileRequestDto;
import com.post.dto.resposne.FileResponseDto;

import java.util.List;

public interface FileService {

    int saveFiles(Long postId, List<FileRequestDto> fileRequestDtos);

    List<FileResponseDto> getFiles(Long postId);

    FileResponseDto getFileById(Long fileId);

    void increaseDownloadCnt(Long fileId);

}
