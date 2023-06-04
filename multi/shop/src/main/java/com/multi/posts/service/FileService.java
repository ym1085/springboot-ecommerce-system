package com.multi.posts.service;

import com.multi.posts.dto.request.FileRequestDto;
import com.multi.posts.dto.resposne.FileResponseDto;

import java.util.List;

public interface FileService {

    int saveFiles(Long postId, List<FileRequestDto> fileRequestDtos);

    List<FileResponseDto> getFiles(Long postId);

    FileResponseDto getFileById(Long fileId);

    void increaseDownloadCnt(Long fileId);

}
