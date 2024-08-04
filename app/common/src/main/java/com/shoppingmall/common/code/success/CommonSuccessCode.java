package com.shoppingmall.common.code.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {
    SUCCESS(OK, "200 OK"),
    SUCCESS_CREATED(CREATED, "리소스가 성공적으로 생성되었습니다."),
    SUCCESS_ACCEPTED(ACCEPTED, "요청이 접수되었으나 아직 처리되지 않았습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}