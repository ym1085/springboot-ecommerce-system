package com.shoppingmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Files {
    private Integer fileId;
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