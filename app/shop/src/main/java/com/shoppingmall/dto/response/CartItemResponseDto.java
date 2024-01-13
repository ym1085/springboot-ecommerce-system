package com.shoppingmall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long cartId;
    private int memberId;
    private int productId;
    private int productCnt;
    private String cartVal;

//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private List<ProductResponseDto>

//    public static CartItemResponseDto toDto(CartVO cart) {
//        return CartItemResponseDto.builder()
//                .cartId(cart.getCartId())
//                .memberId
//    }

}
