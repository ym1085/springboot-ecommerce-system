package com.shoppingmall.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseFactory {

    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, String message, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, message), status
        );
    }

    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, String message, Map<String, String> errorMessage, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, message, errorMessage), status
        );
    }

    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, String message, T result, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, message, result), status
        );
    }

    public static ResponseEntity<CommonResponse> handlerResponseFactory(int result, MessageCode successCode, MessageCode failCode) {
        return (result == 0)
                ? createResponseFactory(failCode.getCode(), failCode.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                : createResponseFactory(successCode.getCode(), successCode.getMessage(), HttpStatus.OK);
    }
}