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

    /**
     * 게시글 수정, 삭제.. 등등 결과값(0: 실패, 1: 성공)에 따라서 분기 처리를 하기 위해 사용
     *
     * @param result
     * @param successCode
     * @param failCode
     * @return
     */
    public static ResponseEntity<CommonResponse> handlerResponseFactory(int result, MessageCode successCode, MessageCode failCode) {
        return (result == 0)
                ? createResponseFactory(failCode.getCode(), failCode.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                : createResponseFactory(successCode.getCode(), successCode.getMessage(), HttpStatus.OK);
    }
}