package com.shoppingmall.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// [Greeting API] https://help.greetinghr.com/reference/common
public class ApiUtils {

    /**
     * [OK] 데이터 없이 응답값 반환
     * @param status    :   HTTP status object
     * @param message   :   HTTP custom message
     */
    public static ResponseEntity<CommonResponse> success(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .code(status.value())
                        .message(message)
                        .build());
    }

    /**
     * [OK] 데이터와 함께 응답값 반환
     * @param status    :   HTTP status object
     * @param message   :   HTTP custom message
     * @param result    :   응답 데이터
     */
    public static <T> ResponseEntity<CommonResponse> success(HttpStatus status, String message, T result) {
        return ResponseEntity
                .status(status)
                .body(CommonResponse.builder()
                        .code(status.value())
                        .message(message)
                        .result(result)
                        .build());
    }
}