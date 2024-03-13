package com.shoppingmall.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // COMMON
    OK("200", HttpStatus.OK, "Success"),
    CREATED("201", HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),
    ACCEPTED("202", HttpStatus.ACCEPTED, "요청이 접수되었으나 아직 처리되지 않았습니다."),

    // MEMBER
    SEND_AUTH_EMAIL("A0101", HttpStatus.OK, "이메일 인증 요청 메일 발송에 성공하였습니다."),
    VERIFY_AUTH_EMAIL("A0102", HttpStatus.OK, "회원 이메일 인증에 성공하였습니다."),
    JOIN_MEMBER("A0103", HttpStatus.OK, "회원가입에 성공하였습니다."),
    NONE_DUPLICATE_MEMBER("A0104", HttpStatus.OK, "중복 회원이 아닙니다."),

    // POST
    SAVE_POST("A0105", HttpStatus.OK, "게시글 등록에 성공하였습니다."),
    DELETE_POST("A0106", HttpStatus.OK, "게시글 삭제에 성공하였습니다."),
    UPDATE_POST("A0107", HttpStatus.OK, "게시글 수정에 성공하였습니다."),
    SAVE_COMMENT("A0108", HttpStatus.OK, "댓글 저장에 성공하였습니다."),
    DELETE_COMMENT("A0109", HttpStatus.OK, "댓글 삭제에 성공하였습니다."),
    UPDATE_COMMENT("A0110", HttpStatus.OK, "댓글 수정에 성공하였습니다."),

    // PRODUCT
    SAVE_PRODUCT("A0111", HttpStatus.OK, "상품 등록에 성공하였습니다."),
    UPDATE_PRODUCT("A0112", HttpStatus.OK, "상품 수정에 성공하였습니다."),
    DELETE_PRODUCT("A0113", HttpStatus.OK, "상품 삭제에 성공하였습니다."),

    // CART
    SAVE_CART("A0114", HttpStatus.OK, "장바구니 상품 추가에 성공하였습니다."),
    UPDATE_CART("A0115", HttpStatus.OK, "장바구니 상품 수정에 성공하였습니다."),
    DELETE_CART("A0116", HttpStatus.OK, "장바구니 상품 삭제에 성공하였습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
