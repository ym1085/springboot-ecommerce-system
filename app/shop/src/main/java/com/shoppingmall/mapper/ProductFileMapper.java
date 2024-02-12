package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.BaseFileSaveRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductFileMapper {

    int saveFiles(List<BaseFileSaveRequestDto> files);

}