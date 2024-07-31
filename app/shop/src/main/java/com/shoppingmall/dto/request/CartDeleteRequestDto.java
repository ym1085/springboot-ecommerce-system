package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDeleteRequestDto {
    private Integer memberId;
    private Integer cartId;
    private String uuid;
}