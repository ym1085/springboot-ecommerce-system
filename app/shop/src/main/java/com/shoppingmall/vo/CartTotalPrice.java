package com.shoppingmall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartTotalPrice {
    private Integer totalPrice; // 사용자 장바구니 1개의 포함된 상품의 전체 합계 가격
    private List<Cart> carts = new LinkedList<>();

    @Builder
    public CartTotalPrice(Integer totalPrice, List<Cart> carts) {
        this.totalPrice = totalPrice;
        this.carts = carts;
    }

    public static CartTotalPrice calculateTotalPrice(List<Cart> carts) {
        if (carts.isEmpty()) {
            return new CartTotalPrice();
        }

        int totalPrice = 0;
        for (Cart cart : carts) {
            totalPrice += (cart.getProduct().getProductPrice() * cart.getAmount());
        }
        return CartTotalPrice.builder()
                .totalPrice(totalPrice)
                .carts(carts)
                .build();
    }
}