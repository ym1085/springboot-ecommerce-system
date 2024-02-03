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

//@ControllerAdvice
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    // Method not supported exception handler
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);

        ErrorResponse response = ErrorResponse
                .create()
                .message(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }

    // Bad request invalid parameter exception handler
    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException e) {
        log.error("handleInvalidParameterException", e);

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse.create()
                .code(errorCode.getHttpStatus().value())
                .message(e.toString())
                .errors(e.getErrors());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // When a class that inherits from customException throws an exception, it will catch it and return an ErrorResponse.
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleAllException", e);

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse.create()
                .code(errorCode.getHttpStatus().value())
                .message(e.toString());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // Handles all exceptions and returns them in the form of an ErrorResponse.
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        log.error("handleException, e.getMessage = {}", e.getMessage());

        ErrorResponse response = ErrorResponse.create()
                .code(-9999)
                .message("[ERROR] 500 Internal Server Error! occurred Exception");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
