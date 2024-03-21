package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.PostFiles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostFileResponseDto extends FileResponseDto {
    private Integer postFileId;
    private Integer postId;

    public static PostFileResponseDto toDto(PostFiles postFiles) {
        return PostFileResponseDto.builder()
                .postFileId(postFiles.getPostFileId())
                .postId(postFiles.getPostId())
                .originFileName(postFiles.getOriginFileName())
                .storedFileName(postFiles.getStoredFileName())
                .filePath(postFiles.getFilePath())
                .fileSize(postFiles.getFileSize())
                .fileExp(postFiles.getFileExp())
                .downloadCnt(postFiles.getDownloadCnt())
                .delYn(postFiles.getDelYn())
                .createDate(postFiles.getCreateDate())
                .deleteDate(postFiles.getDeleteDate())
                .fileAttached(postFiles.getFileAttached())
                .build();
    }

    @Override
    public Integer getFileId() {
        return this.postFileId;
    }

    @Override
    public Integer getId() {
        return this.postId;
    }
}
