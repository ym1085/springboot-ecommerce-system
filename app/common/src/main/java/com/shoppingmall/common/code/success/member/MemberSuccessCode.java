package com.shoppingmall.common.code.success.member;

import com.shoppingmall.common.code.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements SuccessCode {
    SUCCESS_SEND_AUTH_EMAIL(HttpStatus.OK, "이메일 인증 요청 메일 발송에 성공하였습니다."),
    SUCCESS_VERIFY_AUTH_EMAIL(HttpStatus.OK, "회원 이메일 인증에 성공하였습니다."),
    SUCCESS_SAVE_MEMBER(HttpStatus.OK, "회원가입에 성공하였습니다."),
    NONE_DUPLICATE_MEMBER(HttpStatus.OK, "중복 회원이 아닙니다."),
    ;

    private final HttpStatus status;
    private final String message;
}