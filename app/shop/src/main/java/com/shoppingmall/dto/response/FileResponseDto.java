package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class FileResponseDto {
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private int fileSize;
    private String fileExp;
    private int downloadCnt;
    private String delYn;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime deleteDate;
    private String fileAttached;

    public abstract Integer getFileId();
    public abstract Integer getId();
}
