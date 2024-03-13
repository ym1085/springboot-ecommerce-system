package com.shoppingmall.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // COMMON
    BAD_REQUEST("400", HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터가 포함되었습니다."),
    RESOURCE_NOT_FOUND("404", HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("500", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED("405", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),

    // MEMBER
    INACTIVE_MEMBER("E0101", HttpStatus.FORBIDDEN, "멤버가 비활성화 상태입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PWD("E0102", HttpStatus.FORBIDDEN, "멤버 비밀번호를 찾을 수 없습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_ACCOUNT("E0103", HttpStatus.FORBIDDEN, "멤버 계정을 찾을 수 없습니다. 다시 시도해주세요."),
    DUPLICATE_MEMBER_ACCOUNT("E0104", HttpStatus.FORBIDDEN, "멤버 계정이 이미 사용 중입니다. 다시 시도해주세요."),
    SAVE_MEMBER("E0105", HttpStatus.INTERNAL_SERVER_ERROR, "멤버 등록에 실패했습니다. 다시 시도해주세요."),
    AUTHENTICATION_MEMBER_EMAIL("E0106", HttpStatus.FORBIDDEN, "멤버 이메일 인증에 실패했습니다. 다시 시도해주세요."),

    // POST
    SAVE_POST("E0107", HttpStatus.INTERNAL_SERVER_ERROR, "게시글 등록에 실패했습니다. 다시 시도해주세요."),
    UPDATE_POST("E0108", HttpStatus.INTERNAL_SERVER_ERROR, "게시글 수정에 실패했습니다. 다시 시도해주세요."),
    DELETE_POST("E0109", HttpStatus.INTERNAL_SERVER_ERROR, "게시글 삭제에 실패했습니다. 다시 시도해주세요."),
    SAVE_COMMENT("E0110", HttpStatus.INTERNAL_SERVER_ERROR, "댓글 등록에 실패했습니다. 다시 시도해주세요."),
    DELETE_COMMENT("E0111", HttpStatus.INTERNAL_SERVER_ERROR, "댓글 삭제에 실패했습니다. 다시 시도해주세요."),
    UPDATE_COMMENT("E0112", HttpStatus.INTERNAL_SERVER_ERROR, "댓글 수정에 실패했습니다. 다시 시도해주세요."),
    SAVE_FILES("E0113", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다. 다시 시도해주세요."),
    DOWNLOAD_FILES("E0114", HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드에 실패했습니다. 다시 시도해주세요."),
    UPLOAD_FILES("E0115", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업데이트에 실패했습니다. 다시 시도해주세요."),

    // PRODUCT
    SAVE_PRODUCT("E0116", HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록에 실패했습니다. 다시 시도해주세요."),
    UPDATE_PRODUCT("E0117", HttpStatus.INTERNAL_SERVER_ERROR, "상품 수정에 실패했습니다. 다시 시도해주세요."),

    // CART
    SAVE_CART("E0118", HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 등록에 실패했습니다. 다시 시도해주세요."),
    UPDATE_CART("E0119", HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 수정에 실패했습니다. 다시 시도해주세요."),
    DELETE_CART("E0120", HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 삭제에 실패했습니다. 다시 시도해주세요."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
