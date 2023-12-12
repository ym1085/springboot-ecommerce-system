package com.shoppingmall.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private String productName;
    private String productDesc;
    private Integer productPrice;
    private String storedFileName;

    public ProductResponseDto(String productName, String productDesc, Integer productPrice, String storedFileName) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.storedFileName = storedFileName;
    }

}