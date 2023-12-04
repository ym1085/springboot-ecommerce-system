package com.shoppingmall.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiUtils {

    public static <T> ResponseEntity<CommonResponse> success(int code, String message, T result, HttpStatus status) {
        CommonResponse commonResponse = new CommonResponse(code, message, result);
        return new ResponseEntity<>(commonResponse, status);
    }

    public static <T> ResponseEntity<CommonResponse> success(int code, String message, HttpStatus status) {
        CommonResponse commonResponse = new CommonResponse(code, message);
        return new ResponseEntity<>(commonResponse, status);
    }

    // Todo: change CommonResponse -> ErroResponse obj
    public static <T> ResponseEntity<CommonResponse> fail(int code, String message, T result, HttpStatus status) {
        CommonResponse commonResponse = new CommonResponse(code, message, result);
        return new ResponseEntity<>(commonResponse, status);
    }

    // Todo: change CommonResponse -> ErroResponse obj
    public static <T> ResponseEntity<CommonResponse> fail(int code, String message, HttpStatus status) {
        CommonResponse commonResponse = new CommonResponse(code, message);
        return new ResponseEntity<>(commonResponse, status);
    }
}