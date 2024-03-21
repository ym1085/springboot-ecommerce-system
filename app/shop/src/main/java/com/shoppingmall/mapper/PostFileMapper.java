package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.FileSaveRequestDto;
import com.shoppingmall.vo.PostFiles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostFileMapper {

    int saveFiles(List<FileSaveRequestDto> files);

    int deleteFilesByPostId(Integer postId);

    List<PostFiles> getFilesByPostId(Integer postId);

    Optional<PostFiles> getFileByPostFileId(Integer fileId);

    void increaseDownloadCntByFileId(Integer fileId);

    int countFilesByPostId(Integer postId);
}
