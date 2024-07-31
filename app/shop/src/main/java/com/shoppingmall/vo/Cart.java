package com.shoppingmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Cart {
    private Integer cartId;
    private Integer memberId;
    private Integer productId;
    private Integer amount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer totalPricePerCartItem; // 장바구니 상품별 총 합계
    private Product product;
    private Member member;
    private String uuid;
}