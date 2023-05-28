package com.post.dto.resposne;

import com.post.domain.posts.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommentResponseDto {
    private Long commentId;
    private Long parentId;
    private Long postId;
    private String content;
    private Long memberId;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String path;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.parentId = comment.getParentId();
        this.postId = comment.getPostId();
        this.content = comment.getContent();
        this.memberId = comment.getMemberId();
        this.delYn = comment.getDelYn();
        this.createDate = comment.getCreateDate();
        this.updateDate = comment.getUpdateDate();
        this.path = comment.getPath(); // 대댓글 depth ( 1-2 ) -> 1번 최상위 부모 댓글에 2번 댓글이 하위 댓글로 달림
    }
}
