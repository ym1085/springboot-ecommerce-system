package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.PostFiles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDto {
    private Long postFileId;
    private Long postId;
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private int fileSize;
    private String fileType;
    private int downloadCnt;
    private String delYn;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime deleteDate;
    private String fileAttached;

    public static FileResponseDto toDto(PostFiles postFiles) {
        return FileResponseDto.builder()
                .postFileId(postFiles.getPostFileId())
                .postId(postFiles.getPostId())
                .originFileName(postFiles.getOriginFileName())
                .storedFileName(postFiles.getStoredFileName())
                .filePath(postFiles.getFilePath())
                .fileSize(postFiles.getFileSize())
                .fileType(postFiles.getFileType())
                .downloadCnt(postFiles.getDownloadCnt())
                .delYn(postFiles.getDelYn())
                .createDate(postFiles.getCreateDate())
                .deleteDate(postFiles.getDeleteDate())
                .fileAttached(postFiles.getFileAttached())
                .build();
    }
}
