package com.shoppingmall.mapper;

import com.shoppingmall.domain.PostFiles;
import com.shoppingmall.dto.request.FileRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    int saveFiles(List<FileRequestDto> files);

    int deleteFilesByPostId(Long postId);

    List<PostFiles> getFilesByPostId(Long postId);

    int deleteUpdateFilesByPostId(Long postId);

    Optional<PostFiles> getFileByPostFileId(Long fileId);

    void increaseDownloadCntByFileId(Long fileId);
}
