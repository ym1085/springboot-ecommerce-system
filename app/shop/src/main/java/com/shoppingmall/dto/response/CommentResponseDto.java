package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .parentId(comment.getParentId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .memberId(comment.getMemberId())
                .delYn(comment.getDelYn())
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .path(comment.getPath())
                .build();
    }
}
