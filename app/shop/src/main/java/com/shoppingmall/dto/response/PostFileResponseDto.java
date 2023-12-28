package com.shoppingmall.dto.response;

import com.shoppingmall.vo.PostFiles;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFileResponseDto {
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
    private LocalDateTime deleteDate;
    private String fileAttached;

    public static PostFileResponseDto toDto(PostFiles postFiles) {
        return PostFileResponseDto.builder()
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
