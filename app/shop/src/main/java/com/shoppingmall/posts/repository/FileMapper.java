package com.shoppingmall.posts.repository;

import com.shoppingmall.posts.domain.Files;
import com.shoppingmall.posts.dto.request.FileRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    int saveFiles(List<FileRequestDto> files);

    int deleteFilesById(Long postId);

    List<Files> getFiles(Long postId);

    int deleteUpdateFilesById(Long postId);

    Optional<Files> getFileById(Long fileId);

    void increaseDownloadCnt(Long fileId);
}
