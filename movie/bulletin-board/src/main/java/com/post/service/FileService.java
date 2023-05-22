package com.post.service;

import com.post.web.dto.request.FileRequestDto;

import java.util.List;

public interface FileService {

    int saveFiles(Long postId, List<FileRequestDto> fileRequestDtos);

}
