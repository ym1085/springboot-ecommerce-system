package com.shoppingmall.common.code.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonFailureCode implements FailureCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터가 포함되었습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}