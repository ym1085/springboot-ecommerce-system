package com.shoppingmall.exception;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.common.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method Not Supported = {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse
                .create()
                .code(ErrorCode.METHOD_NOT_ALLOWED.getCode()) // custom error code
                .message(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException e) {
        log.error("Invalid Parameter = {}", e.getMessage(), e);

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .create()
                .code(errorCode.getCode())  // custom error code
                .message(errorCode.getMessage())
                .errors(e.getErrors());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("Custom Exception = {}", e.getMessage(), e);

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .create()
                .code(errorCode.getCode())
                .message(errorCode.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected Error = {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse
                .create()
                .code(ErrorCode.ERROR.getCode())
                .message(ErrorCode.ERROR.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
