package com.shoppingmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFiles extends Files {
    private Integer productFileId;
    private Integer productId;
}