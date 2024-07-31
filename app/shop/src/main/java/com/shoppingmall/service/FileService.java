package com.shoppingmall.service;

import com.shoppingmall.mapper.PostFileMapper;
import com.shoppingmall.vo.PostFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    private final PostFileMapper postFileMapper;

    public List<PostFiles> getFilesByPostId(Integer postId) {
        return postFileMapper.getFilesByPostId(postId);
    }

    public PostFiles getFileByPostFileId(Integer postFileId) {
        return postFileMapper.getFileByPostFileId(postFileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
    }

    @Transactional
    public void increaseDownloadCntByFileId(Integer fileId) {
        postFileMapper.increaseDownloadCntByFileId(fileId);
    }
}
