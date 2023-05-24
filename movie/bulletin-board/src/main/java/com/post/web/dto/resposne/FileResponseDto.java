package com.post.web.dto.resposne;

import com.post.domain.posts.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public FileResponseDto(File file) {
        this.fileId = file.getFileId();
        this.postId = file.getPostId();
        this.originalName = file.getOriginalName();
        this.saveName = file.getSaveName();
        this.filePath = file.getFilePath();
        this.fileSize = file.getFileSize();
        this.fileType = file.getFileType();
        this.downloadCnt = file.getDownloadCnt();
        this.delYn = file.getDelYn();
    }
}
