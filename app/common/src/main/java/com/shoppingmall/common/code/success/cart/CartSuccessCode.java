package com.shoppingmall.common.code.success.cart;

import com.shoppingmall.common.code.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartSuccessCode implements SuccessCode {
    SUCCESS_SAVE_CART(HttpStatus.OK, "장바구니 상품 추가에 성공하였습니다."),
    SUCCESS_UPDATE_CART(HttpStatus.OK, "장바구니 상품 수정에 성공하였습니다."),
    SUCCESS_DELETE_CART(HttpStatus.OK, "장바구니 상품 삭제에 성공하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}