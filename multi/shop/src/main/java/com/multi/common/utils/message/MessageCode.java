package com.multi.common.utils.message;

public enum MessageCode {
    //SERVER CODE
    FAIL(0, "FAIL"),
    SUCCESS(1, "SUCCESS"),

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    //SUCCESS CODE
    SUCCESS_GET_POSTS(200, "전체 게시글 조회에 성공 하였습니다."),
    SUCCESS_GET_POST(200, "단일 게시글 조회에 성공 하였습니다."),
    SUCCESS_SAVE_POST(200, "게시글 등록에 성공 하였습니다."),
    SUCCESS_UPDATE_POST(200, "게시글 수정에 성공 하였습니다."),
    SUCCESS_DELETE_POST(200, "게시글 삭제에 성공 하였습니다."),
    SUCCESS_SAVE_COMMENT(200, "댓글 등록에 성공 하였습니다."),
    SUCCESS_DELETE_COMMENT(200, "댓글 삭제에 성공 하였습니다."),
    SUCCESS_UPDATE_COMMENT(200, "댓글 수정에 성공 하였습니다."),
    SUCCESS_SAVE_MEMBER(200, "회원 가입에 성공 하였습니다."),
    SUCCESS_SEND_CODE(200, "이메일 인증코드 전송에 성공 하였습니다."),
    SUCCESS_CERT_EMAIL(200, "이메일 인증에 성공 하였습니다."),

    //FAIL CODE
    FAIL_SAVE_POST(500, "게시글 등록에 실패하였습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST(500, "게시글 수정에 실패하였습니다. 다시 시도해주세요."),
    FAIL_DELETE_POST(500, "게시글 삭제에 실패하였습니다. 다시 시도해주세요."),
    FAIL_SAVE_COMMENT(500, "댓글 등록에 실패하였습니다. 다시 시도해주세요."),
    FAIL_DELETE_COMMENT(500, "댓글 삭제에 실패하였습니다. 다시 시도해주세요."),
    FAIL_UPDATE_COMMENT(500, "댓글 수정에 실패하였습니다. 다시 시도해주세요."),
    FAIL_SAVE_MEMBER(500, "회원 가입에 실패하였습니다. 다시 시도해주세요."),
    FAIL_CERT_EMAIL(500, "이메일 인증에 실패 하였습니다."),

    //VALIDATION CODE
    NOT_FOUND_POST_ID(400, "게시글 번호가 존재하지 않습니다. 다시 시도해주세요."),
    NOT_FOUND_EMAIL(400, "이메일은 반드시 입력되어야 합니다. 다시 시도해주세요."),
    ;

    private final int code;
    private final String message;

    MessageCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
