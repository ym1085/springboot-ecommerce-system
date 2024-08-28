package com.shoppingmall.common.utils;

import com.shoppingmall.common.code.failure.FailureCode;
import com.shoppingmall.common.code.success.SuccessCode;
import com.shoppingmall.common.dto.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtils {

    public static <T> ResponseEntity<BaseResponse<?>> success(SuccessCode code, T data) {
        return ResponseEntity
                .status(code.getStatus())
                .body(BaseResponse.of(code.getStatus().value(), code.getMessage(), data));
    }

    public static ResponseEntity<BaseResponse<?>> success(SuccessCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(BaseResponse.of(code.getStatus().value(), code.getMessage(), true));
    }

    public static <T> ResponseEntity<BaseResponse<?>> success(SuccessCode code, HttpHeaders headers, T data) {
        return ResponseEntity
                .status(code.getStatus())
                .headers(headers)
                .body(BaseResponse.of(code.getStatus().value(), code.getMessage(), data));
    }

    public static <T> ResponseEntity<BaseResponse<?>> failure(FailureCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(BaseResponse.of(code.getStatus().value(), code.getMessage(), false));
    }

    public static <T> ResponseEntity<BaseResponse<?>> failure(HttpStatus status, String code) {
        return ResponseEntity
                .status(status)
                .body(BaseResponse.of(status.value(), code, false));
    }
}