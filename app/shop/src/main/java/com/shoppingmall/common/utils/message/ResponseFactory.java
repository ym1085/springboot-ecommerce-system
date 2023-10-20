package com.shoppingmall.common.utils.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
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

    private static <T> List<T> getSingletonList(T data) {
        return Collections.singletonList(data);
    }

    /**
     *
     * @param result 0: fail, 1: success
     * @param successCode front에 반환 하는 success code, message
     * @param failCode front에 반환 하는 fail code, message
     * @return
     */
    public static ResponseEntity<CommonResponse> handlerResponseFactory(int result, MessageCode successCode, MessageCode failCode) {
        return (result == 0)
                ? createResponseFactory(failCode.getCode(), failCode.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                : createResponseFactory(successCode.getCode(), successCode.getMessage(), HttpStatus.OK);
    }
}