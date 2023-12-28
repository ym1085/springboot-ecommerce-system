package com.shoppingmall.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    private Long postId;
    private Long memberId;
    private Long parentId;
    private Long commentId;
    private String content;

}

