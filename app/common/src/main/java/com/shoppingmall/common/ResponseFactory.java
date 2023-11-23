package com.shoppingmall.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseFactory {

    /**
     * 데이터 없이 응답(response) 하는 경우 사용
     *
     * @param code custom status code
     * @param message custom message(fail or success) - enum MessageCode.java
     * @param status HttpStatus Object
     */
    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, String message, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, message), status
        );
    }

    /**
     * 데이터와 함께 응답(response) 하는 경우 사용
     *
     * @param code custom status code
     * @param message custom message(fail or success) - enum MessageCode.java
     * @param result stores data returned from the actual DB. For example, bulletin board or user data
     * @param status HttpStatus Object
     */
    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, String message, T result, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, message, result), status
        );
    }

    /**
     * 공통 바인딩 오류를 처리해야 하는 경우 사용
     *
     * @param code custom status code
     * @param message custom message(fail or success) - enum MessageCode.java
     * @param errorMessage error message with binding errors
     * @param status HttpStatus Object
     */
    public static <T> ResponseEntity<CommonResponse> createResponseFactory(int code, String message, Map<String, String> errorMessage, HttpStatus status) {
        return new ResponseEntity<>(
                new CommonResponse(code, message, errorMessage), status
        );
    }
}