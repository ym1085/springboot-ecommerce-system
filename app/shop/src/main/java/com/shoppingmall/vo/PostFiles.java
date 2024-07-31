package com.shoppingmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostFiles extends Files {
    private Integer postFileId;
    private Integer postId;
}