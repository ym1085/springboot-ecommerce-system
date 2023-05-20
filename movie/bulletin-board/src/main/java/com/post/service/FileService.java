package com.post.service;

import com.post.web.dto.request.FileSaveRequestDto;

import java.util.List;

public interface FileService {

    void saveFiles(Long postId, List<FileSaveRequestDto> fileSaveRequestDto);

}
