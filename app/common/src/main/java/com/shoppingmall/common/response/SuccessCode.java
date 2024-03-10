package com.shoppingmall.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // [COMMON]
    OK(200, HttpStatus.OK, "200 OK"),
    CREATED(201, HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),
    ACCEPTED(202, HttpStatus.ACCEPTED, "요청이 접수되었으나 아직 처리되지 않았습니다."),

    // [MEMBER]
    SEND_AUTH_EMAIL(101, HttpStatus.OK, "이메일 인증 요청 메일 발송에 성공하였습니다."),
    VERIFY_AUTH_EMAIL(102, HttpStatus.OK, "회원 이메일 인증에 성공하였습니다."),
    JOIN_MEMBER(103, HttpStatus.OK, "회원가입에 성공하였습니다."),
    NONE_DUPLICATE_MEMBER(104, HttpStatus.OK, "중복 회원이 아닙니다."),

    // [POST]
    SAVE_POST(201, HttpStatus.OK, "게시글 등록에 성공하였습니다."),
    DELETE_POST(202, HttpStatus.OK, "게시글 삭제에 성공하였습니다."),
    UPDATE_POST(203, HttpStatus.OK, "게시글 수정에 성공하였습니다."),
    SAVE_COMMENT(204, HttpStatus.OK, "댓글 저장에 성공하였습니다."),
    DELETE_COMMENT(205, HttpStatus.OK, "댓글 삭제에 성공하였습니다."),
    UPDATE_COMMENT(206, HttpStatus.OK, "댓글 수정에 성공하였습니다."),

    // [PRODUCT]
    SAVE_PRODUCT(300, HttpStatus.OK, "상품 등록에 성공하였습니다."),
    UPDATE_PRODUCT(301, HttpStatus.OK, "상품 수정에 성공하였습니다."),
    DELETE_PRODUCT(302, HttpStatus.OK, "상품 삭제에 성공하였습니다."),

    // [CART]
    SAVE_CART(400, HttpStatus.OK, "장바구니에 상품을 추가하였습니다."),
    UPDATE_CART(401, HttpStatus.OK, "장바구니 상품을 수정하였습니다."),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
