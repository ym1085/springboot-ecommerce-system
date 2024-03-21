package com.shoppingmall.vo;

import lombok.Builder;
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

    @Builder
    public Comment(Integer commentId, Integer parentId, Integer postId, String content, Integer memberId, String delYn, String path, LocalDateTime createDate, LocalDateTime updateDate) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.postId = postId;
        this.content = content;
        this.memberId = memberId;
        this.delYn = delYn;
        this.path = path;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
