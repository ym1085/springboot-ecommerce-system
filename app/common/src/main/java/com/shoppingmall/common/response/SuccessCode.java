package com.shoppingmall.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // [COMMON]
    OK(HttpStatus.OK, "200 OK"),
    CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "요청이 접수되었으나 아직 처리되지 않았습니다."),

    // [MEMBER]
    SUCCESS_SEND_AUTH_EMAIL(HttpStatus.OK, "이메일 인증 요청 메일 발송에 성공하였습니다."),
    SUCCESS_VERIFY_AUTH_EMAIL(HttpStatus.OK, "회원 이메일 인증에 성공하였습니다."),
    SUCCESS_JOIN_MEMBER(HttpStatus.OK, "회원가입에 성공하였습니다."),
    NONE_DUPLICATE_MEMBER(HttpStatus.OK, "중복 회원이 아닙니다."),

    // [POST]
    SUCCESS_SAVE_POST(HttpStatus.OK, "게시글 등록에 성공하였습니다."),
    SUCCESS_DELETE_POST(HttpStatus.OK, "게시글 삭제에 성공하였습니다."),
    SUCCESS_UPDATE_POST(HttpStatus.OK, "게시글 수정에 성공하였습니다."),
    SUCCESS_SAVE_COMMENT(HttpStatus.OK, "댓글 저장에 성공하였습니다."),
    SUCCESS_DELETE_COMMENT(HttpStatus.OK, "댓글 삭제에 성공하였습니다."),
    SUCCESS_UPDATE_COMMENT(HttpStatus.OK, "댓글 수정에 성공하였습니다."),

    // [PRODUCT]
    // [CART]
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
