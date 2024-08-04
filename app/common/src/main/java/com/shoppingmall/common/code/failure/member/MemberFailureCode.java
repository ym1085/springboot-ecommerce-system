package com.shoppingmall.common.code.failure.member;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberFailureCode implements FailureCode {
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "멤버가 비활성화 상태입니다. 다시 시도해주세요."),
    NOT_LOGIN_MEMBER(HttpStatus.FORBIDDEN, "로그인 상태가 아닙니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PWD(HttpStatus.FORBIDDEN, "멤버 비밀번호를 찾을 수 없습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_ACCOUNT(HttpStatus.FORBIDDEN, "멤버 계정을 찾을 수 없습니다. 다시 시도해주세요."),
    DUPLICATE_MEMBER_ACCOUNT(HttpStatus.FORBIDDEN, "멤버 계정이 이미 사용 중입니다. 다시 시도해주세요."),
    FAIL_SAVE_MEMBER(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 등록에 실패했습니다. 다시 시도해주세요."),
    AUTHENTICATION_MEMBER_EMAIL(HttpStatus.FORBIDDEN, "멤버 이메일 인증에 실패했습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus status;
    private final String message;
}