package com.shoppingmall.dto.request;

import com.shoppingmall.vo.Cart;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailRequestDto {

    private Integer cartId;
    private Integer memberId;
    private Integer productId;
    private String uuid;

    public Cart toEntity() {
        return Cart.builder()
                .cartId(cartId)
                .memberId(memberId)
                .productId(productId)
                .uuid(uuid)
                .build();
    }
}