package com.post.service.impl;

import com.post.constant.ResponseCode;
import com.post.domain.posts.File;
import com.post.repository.post.FileMapper;
import com.post.service.FileService;
import com.post.web.dto.request.FileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

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
    public List<File> getFiles(Long postId) {
        return fileMapper.getFiles(postId);
    }
}
