package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.PostFiles;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deleteDate;
    private String fileAttached;

    public PostFileResponseDto(PostFiles postFiles) {
        this.postFileId = postFiles.getPostFileId();
        this.postId = postFiles.getPostId();
        this.originFileName = postFiles.getOriginFileName();
        this.storedFileName = postFiles.getStoredFileName();
        this.filePath = postFiles.getFilePath();
        this.fileSize = postFiles.getFileSize();
        this.fileType = postFiles.getFileType();
        this.downloadCnt = postFiles.getDownloadCnt();
        this.delYn = postFiles.getDelYn();
        this.createDate = postFiles.getCreateDate();
        this.deleteDate = postFiles.getDeleteDate();
        this.fileAttached = postFiles.getFileAttached();
    }
}
