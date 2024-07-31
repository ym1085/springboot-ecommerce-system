package com.shoppingmall.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    private Integer commentId;
    private Integer parentId;
    private Integer postId;
    private String content;
    private Integer memberId;
    private String delYn;
    private String path;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
