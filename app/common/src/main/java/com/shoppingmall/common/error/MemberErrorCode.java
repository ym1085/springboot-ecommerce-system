package com.shoppingmall.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "Member is inactive. Please try again."),
    NOT_FOUND_MEMBER_PWD(HttpStatus.FORBIDDEN, "Member password is not found. Please try again."),
    NOT_FOUND_MEMBER_ACCOUNT(HttpStatus.FORBIDDEN, "Member account is not found. Please try again."),
    DUPLICATE_MEMBER_ACCOUNT(HttpStatus.FORBIDDEN, "Member account is already in use. Please try again."),
    FAIL_SAVE_MEMBER(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register the member. Please try again."),
    FAIL_AUTHENTICATION_MEMBER_EMAIL(HttpStatus.FORBIDDEN, "Failed to authentication member email. Please try again."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
