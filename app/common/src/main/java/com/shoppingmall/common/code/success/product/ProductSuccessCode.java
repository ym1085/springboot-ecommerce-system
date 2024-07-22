package com.shoppingmall.common.code.success.product;

import com.shoppingmall.common.code.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {
    SAVE_PRODUCT(HttpStatus.OK, "상품 등록에 성공하였습니다."),
    UPDATE_PRODUCT(HttpStatus.OK, "상품 수정에 성공하였습니다."),
    DELETE_PRODUCT(HttpStatus.OK, "상품 삭제에 성공하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}