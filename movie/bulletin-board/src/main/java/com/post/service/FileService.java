package com.post.service;

import com.post.web.dto.request.FileRequestDto;
import com.post.web.dto.resposne.FileResponseDto;

import java.util.List;

public interface FileService {

    int saveFiles(Long postId, List<FileRequestDto> fileRequestDtos);

    FileResponseDto getFileById(Long fileId);

    void increaseDownloadCnt(Long fileId);

}
