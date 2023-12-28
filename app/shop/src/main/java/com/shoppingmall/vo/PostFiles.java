package com.shoppingmall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostFiles {
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
}
