package com.shoppingmall.common.code.failure.product;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductFailureCode implements FailureCode {
    NOT_FOUND_PRODUCT(HttpStatus.INTERNAL_SERVER_ERROR, "상품이 존재하지 않습니다"),
    FAIL_SAVE_PRODUCT(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_PRODUCT(HttpStatus.INTERNAL_SERVER_ERROR, "상품 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_DUPLICATE_PRODUCT_NAME(HttpStatus.INTERNAL_SERVER_ERROR, "상품명이 중복되었습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus status;
    private final String message;
}