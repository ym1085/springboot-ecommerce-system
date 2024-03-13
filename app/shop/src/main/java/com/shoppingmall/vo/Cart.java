package com.shoppingmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Cart {
    private Long cartId;
    private Long memberId;
    private Long productId;
    private Long amount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer totalPricePerCartItem; // 장바구니 상품별 총 합계
    private Product product;
    private Member member;

    @Builder
    public Cart(Long cartId, Long memberId, Long productId, Long amount, LocalDateTime createDate, LocalDateTime updateDate, Integer totalPricePerCartItem, Product product, Member member) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.productId = productId;
        this.amount = amount;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.totalPricePerCartItem = totalPricePerCartItem;
        this.product = product;
        this.member = member;
    }
}