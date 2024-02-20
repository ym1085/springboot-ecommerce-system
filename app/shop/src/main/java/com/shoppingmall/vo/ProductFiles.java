package com.shoppingmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProductFiles {
    private Long productFileId;
    private Long productId;
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private int fileSize;
    private String fileType;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;
    private String delYn;
    private String fileAttached;
    private int downloadCnt;

    @Builder
    public ProductFiles(
            Long productFileId,
            Long productId,
            String originFileName,
            String storedFileName,
            String filePath,
            int fileSize,
            String fileType,
            LocalDateTime createDate,
            LocalDateTime deleteDate,
            String delYn,
            String fileAttached,
            int downloadCnt
    ) {
        this.productFileId = productFileId;
        this.productId = productId;
        this.originFileName = originFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.createDate = createDate;
        this.deleteDate = deleteDate;
        this.delYn = delYn;
        this.fileAttached = fileAttached;
        this.downloadCnt = downloadCnt;
    }
}
