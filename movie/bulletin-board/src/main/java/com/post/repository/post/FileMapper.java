package com.post.repository.post;

import com.post.web.dto.request.FileSaveRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    void saveFiles(List<FileSaveRequestDto> files);

}
