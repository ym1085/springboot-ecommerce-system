package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponseDto {
    private Long cartId;
    private Long memberId;
    private Long productId;
    private Long amount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer totalPricePerCartItem; // 장바구니 상품별 총 합계

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProductDetailResponseDto product;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private MemberResponseDto member;

    public static CartResponseDto toDto(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getCartId())
                .memberId(cart.getMemberId())
                .productId(cart.getProductId())
                .amount(cart.getAmount())
                .createDate(cart.getCreateDate())
                .updateDate(cart.getUpdateDate())
                .totalPricePerCartItem(cart.getTotalPricePerCartItem())
                .product(cart.getProduct() != null ? ProductDetailResponseDto.toDto(cart.getProduct()) : new ProductDetailResponseDto())
                .member(cart.getMember() != null ? MemberResponseDto.toDto(cart.getMember()) : new MemberResponseDto())
                .build();
    }
}