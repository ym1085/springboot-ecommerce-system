package com.shoppingmall.dto.request;

import lombok.*;

@Getter
@Setter
public class CommentUpdateRequestDto {
    private Integer postId;
    private Integer memberId;
    private Integer parentId;
    private Integer commentId;
    private String content;
}

