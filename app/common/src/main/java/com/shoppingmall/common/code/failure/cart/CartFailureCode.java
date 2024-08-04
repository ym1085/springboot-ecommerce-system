package com.shoppingmall.common.code.failure.cart;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartFailureCode implements FailureCode {
    FAIL_SAVE_CART(HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_CART(HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_CART(HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 삭제에 실패했습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus status;
    private final String message;
}