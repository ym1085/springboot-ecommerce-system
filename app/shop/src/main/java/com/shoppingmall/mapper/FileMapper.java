package com.shoppingmall.mapper;

import com.shoppingmall.vo.PostFilesVO;
import com.shoppingmall.dto.request.FileRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    int saveFiles(List<FileRequestDto> files);

    int deleteFilesByPostId(Long postId);

    List<PostFilesVO> getFilesByPostId(Long postId);

    int deleteUpdateFilesByPostId(Long postId);

    Optional<PostFilesVO> getFileByPostFileId(Long fileId);

    void increaseDownloadCntByFileId(Long fileId);

    int countFilesByPostId(Long postId);
}
