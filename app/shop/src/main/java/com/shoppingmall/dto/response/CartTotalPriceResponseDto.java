package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartTotalPriceResponseDto {
    private Integer totalPrice; // 사용자 장바구니 1개의 포함된 상품의 전체 합계 가격
    private List<CartDetailResponseDto> carts = new ArrayList<>();

    @Builder
    public CartTotalPriceResponseDto(Integer totalPrice, List<CartDetailResponseDto> carts) {
        this.totalPrice = totalPrice;
        this.carts = carts;
    }

    public static CartTotalPriceResponseDto calculateTotalPrice(List<CartDetailResponseDto> carts) {
        if (carts.isEmpty()) {
            return new CartTotalPriceResponseDto();
        }

        int totalPrice = 0;
        for (CartDetailResponseDto cart : carts) {
            totalPrice += (cart.getProduct().getProductPrice() * cart.getAmount());
        }

        return CartTotalPriceResponseDto.builder()
                .totalPrice(totalPrice)
                .carts(carts)
                .build();
    }
}