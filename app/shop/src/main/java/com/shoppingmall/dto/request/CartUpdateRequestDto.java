package com.shoppingmall.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartUpdateRequestDto {

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Integer productId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private Integer amount;
    private Integer memberId;
    private Integer cartId;
    private String uuid;
}