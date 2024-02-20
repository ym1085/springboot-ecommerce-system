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
        log.trace("Stack Trace Log = {}", e);
        log.error("Method Not Supported = {}", e.getMessage());

        ErrorResponse response = ErrorResponse
                .create()
                .code(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus().value())
                .message(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException e) {
        log.trace("Stack Trace Log = {}", e);
        log.error("Invalid Parameter = {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .create()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .errors(e.getErrors());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.trace("Stack Trace Log = {}", e);
        log.error("Custom Exception = {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .create()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.trace("Stack Trace Log = {}", e);
        log.error("Unexpected Error = {}", e.getMessage());

        ErrorResponse response = ErrorResponse
                .create()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("내부 서버 오류입니다. 나중에 다시 시도해주세요.");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
