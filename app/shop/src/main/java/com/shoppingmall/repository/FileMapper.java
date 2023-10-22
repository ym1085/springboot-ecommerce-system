package com.shoppingmall.repository;

import com.shoppingmall.domain.PostFiles;
import com.shoppingmall.dto.request.FileRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    int saveFiles(List<FileRequestDto> files);

    int deleteFilesById(Long postId);

    List<PostFiles> getFiles(Long postId);

    int deleteUpdateFilesById(Long postId);

    Optional<PostFiles> getFileById(Long fileId);

    void increaseDownloadCnt(Long fileId);
}
