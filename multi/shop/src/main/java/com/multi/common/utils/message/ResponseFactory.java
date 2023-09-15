package com.multi.common.utils.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class ResponseFactory {

    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, T data, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, getSingletonList(data)), status
        );
    }

    private static <T> List<T> getSingletonList(T data) {
        return Collections.singletonList(data);
    }

    public static ResponseEntity<CommonResponse> handlerResponseFactory(int result, MessageCode successCode, MessageCode failCode) {
        return (result == 0)
                ? createResponseFactory(failCode.getCode(), failCode.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                : createResponseFactory(successCode.getCode(), successCode.getMessage(), HttpStatus.OK);
    }
}
