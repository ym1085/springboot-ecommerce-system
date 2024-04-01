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

    @Builder
    public Cart(Integer cartId, Integer memberId, Integer productId, Integer amount, LocalDateTime createDate, LocalDateTime updateDate, Integer totalPricePerCartItem, Product product, Member member, String uuid) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.productId = productId;
        this.amount = amount;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.totalPricePerCartItem = totalPricePerCartItem;
        this.product = product;
        this.member = member;
        this.uuid = uuid;
    }
}