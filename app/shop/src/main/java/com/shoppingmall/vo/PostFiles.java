package com.shoppingmall.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostFiles {
    private Long postFileId;
    private Long postId;
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private int fileSize;
    private String fileExp;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;
    private String delYn;
    private String fileAttached;
    private int downloadCnt;
}
