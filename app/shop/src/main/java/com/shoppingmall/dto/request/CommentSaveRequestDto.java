package com.shoppingmall.dto.request;

import lombok.*;

@Getter
@Setter
public class CommentSaveRequestDto {
    private Integer postId;
    private Integer memberId;
    private Integer parentId;
    private Integer commentId;
    private String content;
}

