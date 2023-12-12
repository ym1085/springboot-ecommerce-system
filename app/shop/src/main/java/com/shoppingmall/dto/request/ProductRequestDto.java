package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String productName;
    private String productDesc;
    private Integer productPrice;
    private String storedFileName; // 상품 이미지

    public ProductRequestDto(String productName, String productDesc, Integer productPrice, String storedFileName) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.storedFileName = storedFileName;
    }

}