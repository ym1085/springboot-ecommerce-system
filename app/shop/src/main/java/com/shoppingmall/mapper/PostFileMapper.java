package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.BaseFileSaveRequestDto;
import com.shoppingmall.vo.PostFiles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostFileMapper {

    int saveFiles(List<BaseFileSaveRequestDto> files);

    int deleteFilesByPostId(Long postId);

    List<PostFiles> getFilesByPostId(Long postId);

    int deleteUpdateFilesByPostId(Long postId);

    Optional<PostFiles> getFileByPostFileId(Long fileId);

    void increaseDownloadCntByFileId(Long fileId);

    int countFilesByPostId(Long postId);
}
