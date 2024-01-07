package com.shoppingmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentVO {
    private Long commentId;
    private Long parentId;
    private Long postId;
    private String content;
    private Long memberId;
    private String delYn;
    private String path;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public CommentVO(Long commentId, Long parentId, Long postId, String content, Long memberId,
                     String delYn, String path, LocalDateTime createDate, LocalDateTime updateDate) {
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
