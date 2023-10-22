package com.shoppingmall.dto.response;

import com.shoppingmall.domain.PostFiles;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FileResponseDto {
    private Long postFileId;
    private Long postId;
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private String fileSize;
    private String fileType;
    private int downloadCnt;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;

    public FileResponseDto(PostFiles postFiles) {
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
    }
}
