package com.shoppingmall.dto.request;

import com.shoppingmall.vo.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDeleteRequestDto {

    private Integer memberId;
    private Integer cartId;
    private String uuid;

    public Cart toEntity() {
        return Cart.builder()
                .cartId(cartId)
                .memberId(memberId)
                .uuid(uuid)
                .build();
    }
}