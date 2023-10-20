package com.shoppingmall.common.utils.message;

public enum MessageCode {
    //SERVER CODE
    FAIL(0, "FAIL"),
    SUCCESS(1, "SUCCESS"),

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    //SUCCESS CODE
    SUCCESS_GET_POSTS(1000, "전체 게시글 조회에 성공 하였습니다."),
    SUCCESS_GET_POST(1001, "단일 게시글 조회에 성공 하였습니다."),
    SUCCESS_SAVE_POST(1002, "게시글 등록에 성공 하였습니다."),
    SUCCESS_UPDATE_POST(1003, "게시글 수정에 성공 하였습니다."),
    SUCCESS_DELETE_POST(1004, "게시글 삭제에 성공 하였습니다."),
    SUCCESS_SAVE_COMMENT(1005, "댓글 등록에 성공 하였습니다."),
    SUCCESS_DELETE_COMMENT(1006, "댓글 삭제에 성공 하였습니다."),
    SUCCESS_UPDATE_COMMENT(1007, "댓글 수정에 성공 하였습니다."),
    SUCCESS_SAVE_MEMBER(1008, "회원 가입에 성공 하였습니다."),
    SUCCESS_SEND_CODE(1009, "이메일 인증코드 전송에 성공 하였습니다."),
    SUCCESS_CERT_EMAIL(1010, "이메일 인증에 성공 하였습니다."),
    SUCCESS_DUPL_ACCOUNT(1010, "사용 가능한 계정입니다."),

    //FAIL CODE
    FAIL_SAVE_POST(2000, "게시글 등록에 실패하였습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST(2001, "게시글 수정에 실패하였습니다. 다시 시도해주세요."),
    FAIL_DELETE_POST(2002, "게시글 삭제에 실패하였습니다. 다시 시도해주세요."),
    FAIL_SAVE_COMMENT(2003, "댓글 등록에 실패하였습니다. 다시 시도해주세요."),
    FAIL_DELETE_COMMENT(2004, "댓글 삭제에 실패하였습니다. 다시 시도해주세요."),
    FAIL_UPDATE_COMMENT(2005, "댓글 수정에 실패하였습니다. 다시 시도해주세요."),
    FAIL_SAVE_MEMBER(2006, "회원 가입에 실패하였습니다. 다시 시도해주세요."),
    FAIL_CERT_EMAIL(2007, "이메일 인증에 실패 하였습니다. 다시 시도해주세요."),
    FAIL_DUPL_MEMBER(2008, "중복된 회원 입니다. 다시 시도해주세요."),
    FAIL_CHECK_DUPL_MEMBER(2009, "아이디 중복 체크에 실패하였습니다. 다시 시도해주세요."),

    //VALIDATION CODE
    NOT_FOUND_POST_ID(3000, "게시글 번호가 존재하지 않습니다. 다시 시도해주세요."),
    NOT_FOUND_ACCOUNT(3001, "계정은 반드시 입력되어야 합니다. 다시 시도해주세요."),
    NOT_FOUND_EMAIL(3002, "이메일은 반드시 입력되어야 합니다. 다시 시도해주세요."),
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
