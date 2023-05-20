package com.post.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class File {
    private Long fileId;
    private Long postId;
    private String originalName;
    private String saveName;
    private String filePath;
    private String fileSize;
    private String fileType;
    private int downloadCnt;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;
}
