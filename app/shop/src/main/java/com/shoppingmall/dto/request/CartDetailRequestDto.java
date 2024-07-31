package com.shoppingmall.dto.request;

import lombok.*;

@Getter
@Setter
public class CartDetailRequestDto {
    private Integer cartId;
    private Integer memberId;
    private Integer productId;
    private String uuid;
}