package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteRequestDto {
    private Integer postId;
    private Integer commentId;
}

