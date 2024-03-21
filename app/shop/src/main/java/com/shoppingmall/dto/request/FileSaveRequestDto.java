package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class FileSaveRequestDto {
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private long fileSize;
    private String fileExp;
    private String fileAttached;

    public abstract void setId(Integer id);
    public abstract Integer getFileId();
    public abstract Integer getId();
}
