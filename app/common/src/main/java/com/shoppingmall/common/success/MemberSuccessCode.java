package com.shoppingmall.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements SuccessCode {
    SUCCESS_SEND_AUTH_EMAIL(HttpStatus.OK, "Success send authenticated email."),
    SUCCESS_VERIFY_AUTH_EMAIL(HttpStatus.OK, "Success verify authenticated email."),
    SUCCESS_JOIN_MEMBER(HttpStatus.OK, "Success join member."),
    NONE_DUPLICATE_MEMBER(HttpStatus.OK, "None duplicate member."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
