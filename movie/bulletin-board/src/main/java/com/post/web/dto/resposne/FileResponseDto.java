package com.post.web.dto.resposne;

import com.post.domain.posts.Files;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FileResponseDto {
    private Long fileId;
    private Long postId;
    private String originalName;
    private String saveName;
    private String filePath;
    private String fileSize;
    private String fileType;
    private int downloadCnt;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;

    public FileResponseDto(Files file) {
        this.fileId = file.getFileId();
        this.postId = file.getPostId();
        this.originalName = file.getOriginalName();
        this.saveName = file.getSaveName();
        this.filePath = file.getFilePath();
        this.fileSize = file.getFileSize();
        this.fileType = file.getFileType();
        this.downloadCnt = file.getDownloadCnt();
        this.delYn = file.getDelYn();
        this.createDate = file.getCreateDate();
        this.deleteDate = file.getDeleteDate();
    }
}
