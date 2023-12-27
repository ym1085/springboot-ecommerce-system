package com.shoppingmall.vo;

import com.shoppingmall.dto.request.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    private Long commentId;
    private Long parentId;
    private Long postId;
    private String content;
    private Long memberId;
    private String delYn;
    private String path;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Comment(CommentRequestDto commentRequestDto) {
        this.commentId = commentRequestDto.getCommentId();
        this.parentId = commentRequestDto.getParentId();
        this.postId = commentRequestDto.getPostId();
        this.content = commentRequestDto.getContent();
        this.memberId = commentRequestDto.getMemberId();
    }
}
