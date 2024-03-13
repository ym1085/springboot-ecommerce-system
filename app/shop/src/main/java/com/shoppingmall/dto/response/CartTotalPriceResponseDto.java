package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"totalPriceCartItem", "cartItems"})
public class CartTotalPriceResponseDto {
    private Integer totalPriceCartItem; // 장바구니 모든 상품 총 합계
    private List<CartResponseDto> cartItems;
}
