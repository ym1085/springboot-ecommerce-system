package com.shoppingmall.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// [Greeting API] https://help.greetinghr.com/reference/common
public class ApiUtils {

    /**
     * [OK] 데이터 없이 응답값 반환
     * @param code      :   HTTP Status code
     * @param message   :   HTTP custom message
     * @param status    :   HTTP status code
     */
    public static ResponseEntity<CommonResponse> success(int code, String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .code(code)
                        .message(message)
                        .build());
    }

    /**
     * [OK] 데이터와 함께 응답값 반환
     * @param code      :   HTTP Status code
     * @param message   :   HTTP custom message
     * @param status    :   HTTP status code
     * @param result    :   응답 데이터
     */
    public static <T> ResponseEntity<CommonResponse> success(int code, String message, T result, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .code(code)
                        .message(message)
                        .result(result)
                        .build());
    }

    /**
     * [FAIL] 데이터 없이 응답값 반환
     * @param code      :   HTTP Status code
     * @param message   :   HTTP custom message
     * @param status    :   HTTP status code
     */
    public static ResponseEntity<CommonResponse> fail(int code, String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .code(code)
                        .message(message)
                        .build());
    }
}