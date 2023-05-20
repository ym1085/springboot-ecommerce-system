package com.post.service.impl;

import com.post.repository.post.FileMapper;
import com.post.service.FileService;
import com.post.web.dto.request.FileSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileMapper mapper;

    @Override
    public void saveFiles(Long postId, List<FileSaveRequestDto> files) {
        if (CollectionUtils.isEmpty(files) || postId == null) {
            return;
        }
        for (FileSaveRequestDto file : files) {
            file.setPostId(postId);
        }
        mapper.saveFiles(files);
    }
}
