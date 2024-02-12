package com.shoppingmall.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // [COMMON]
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터가 포함되었습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),

    // [MEMBER]
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "멤버가 비활성화 상태입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PWD(HttpStatus.FORBIDDEN, "멤버 비밀번호를 찾을 수 없습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_ACCOUNT(HttpStatus.FORBIDDEN, "멤버 계정을 찾을 수 없습니다. 다시 시도해주세요."),
    DUPLICATE_MEMBER_ACCOUNT(HttpStatus.FORBIDDEN, "멤버 계정이 이미 사용 중입니다. 다시 시도해주세요."),
    FAIL_SAVE_MEMBER(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_AUTHENTICATION_MEMBER_EMAIL(HttpStatus.FORBIDDEN, "멤버 이메일 인증에 실패했습니다. 다시 시도해주세요."),

    // [POST]
    FAIL_SAVE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 삭제에 실패했습니다. 다시 시도해주세요."),
    FAIL_SAVE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 삭제에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_SAVE_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다. 다시 시도해주세요."),
    FAIL_DOWNLOAD_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPLOAD_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업데이트에 실패했습니다. 다시 시도해주세요."),

    // [PRODUCT]
    FAIL_SAVE_PRODUCT(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록에 실패했습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
