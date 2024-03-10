package com.shoppingmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Cart {
    // Cart에 들어가야 하는 필드 전부 만들어봐
    private Long cartId;
    private Long productId;
    private Long memberId;
    private Integer amount;
    private LocalDateTime createDate;

    @Builder
    public Cart(Long cartId, Long productId, Long memberId, Integer amount, LocalDateTime createDate) {
        this.cartId = cartId;
        this.productId = productId;
        this.memberId = memberId;
        this.amount = amount;
        this.createDate = createDate;
    }
}
