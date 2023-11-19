package com.shoppingmall.common;

public enum MessageCode {
    FAIL(0, "FAIL"),
    SUCCESS(1, "SUCCESS"),

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    //////////////////////////////////////////////////////////////////////////////////////
    // SUCCESS CODE
    //////////////////////////////////////////////////////////////////////////////////////
    // POST
    SUCCESS_GET_POSTS(1000, "전체 게시글 조회에 성공 하였습니다."),
    SUCCESS_GET_POST(1001, "단일 게시글 조회에 성공 하였습니다."),
    SUCCESS_SAVE_POST(1002, "게시글 등록에 성공 하였습니다."),
    SUCCESS_UPDATE_POST(1003, "게시글 수정에 성공 하였습니다."),
    SUCCESS_DELETE_POST(1004, "게시글 삭제에 성공 하였습니다."),

    // MEMBER
    SUCCESS_SAVE_MEMBER(1100, "회원 가입에 성공 하였습니다."),
    SUCCESS_SEND_EMAIL(1101, "이메일 인증코드 전송에 성공 하였습니다."),
    SUCCESS_CERT_EMAIL(1102, "이메일 인증에 성공 하였습니다."),
    SUCCESS_DUPL_ACCOUNT(1103, "사용 가능한 계정입니다."),

    // COMMENT
    SUCCESS_SAVE_COMMENT(1200, "댓글 등록에 성공 하였습니다."),
    SUCCESS_DELETE_COMMENT(1201, "댓글 삭제에 성공 하였습니다."),
    SUCCESS_UPDATE_COMMENT(1202, "댓글 수정에 성공 하였습니다."),

    //////////////////////////////////////////////////////////////////////////////////////
    // FAIL CODE
    //////////////////////////////////////////////////////////////////////////////////////
    // POST
    FAIL_SAVE_POST(2000, "게시글 등록에 실패하였습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST(2001, "게시글 수정에 실패하였습니다. 다시 시도해주세요."),
    FAIL_DELETE_POST(2002, "게시글 삭제에 실패하였습니다. 다시 시도해주세요."),
    FAIL_SAVE_COMMENT(2003, "댓글 등록에 실패하였습니다. 다시 시도해주세요."),
    FAIL_DELETE_COMMENT(2004, "댓글 삭제에 실패하였습니다. 다시 시도해주세요."),
    FAIL_UPDATE_COMMENT(2005, "댓글 수정에 실패하였습니다. 다시 시도해주세요."),

    // MEMBER
    FAIL_SAVE_MEMBER(2100, "회원 가입에 실패하였습니다. 다시 시도해주세요."),
    FAIL_CERT_EMAIL(2101, "이메일 인증에 실패 하였습니다. 다시 시도해주세요."),
    FAIL_DUPL_MEMBER(2102, "중복된 회원 입니다. 다시 시도해주세요."),
    FAIL_CHECK_DUPL_MEMBER(2103, "아이디 중복 체크에 실패하였습니다. 다시 시도해주세요."),

    //////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION CODE
    //////////////////////////////////////////////////////////////////////////////////////
    // POST
    NOT_FOUND_POST_ID(3000, "게시글 번호가 존재하지 않습니다. 다시 시도해주세요."),
    NOT_FOUND_POST_TITLE(3001, "제목은 반드시 입력되어야 합니다. 다시 시도해주세요."),
    NOT_FOUND_POST_CONTENT(3002, "내용은 반드시 입력되어야 합니다. 다시 시도해주세요."),
    NOT_FOUND_POST_FIXED_YN(3003, "고정글 여부는 (Y/N) 으로 지정 되어야 합니다. 다시 시도해주세요."),
    INVALID_POST_TITLE(3004, "제목은 20자를 초과할 수 없습니다. 다시 시도해주세요."),
    INVALID_POST_CONTENT(3005, "내용은 1000자를 초과할 수 없습니다. 다시 한번 시도해주세요."),

    // MEMBER
    NOT_FOUND_MEMBER_NAME(3100, "이름은 필수 입력 항목 입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_ACCOUNT(3101, "ID는 필수 입력 항목 입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PASSWORD(3102, "비밀번호는 필수 입력 항목 입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_EMAIL(3103, "이메일은 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PHONE(3104, "휴대폰 번호는 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_EMAIL_CERT_YN(3105, "이메일 인증이 진행되지 않았습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_DUPL_ACCOUNT_CERT_YN(3106, "아이디 중복 인증이 진행되지 않았습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_GENDER(3107, "성별을 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_BIRTHDATE(3108, "아이디 중복 인증이 진행되지 않았습니다. 다시 시도해주세요."),
    INVALID_MEMBER_NAME(3109, "두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요. 다시 시도해주세요."),
    INVALID_MEMBER_ACCOUNT(3110, "30자 이하의 ID만 입력 가능합니다. 다시 시도해주세요."),
    INVALID_MEMBER_PASSWORD(3111, "영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요. 다시 시도해주세요."),
    INVALID_MEMBER_EMAIL(3112, "올바른 메일 형식이 아닙니다. 다시 시도해주세요. 다시 시도해주세요."),
    INVALID_MEMBER_PHONE(3113, "올바른 휴대폰번호 형식이 아닙니다. 다시 시도해주세요."),
    INVALID_MEMBER_EMAIL_CERT_YN(3114, "사용자 인증에 실패하였습니다. 다시 시도해주세요."),
    INVALID_MEMBER_DUPL_ACCOUNT_CERT_YN(3115, "사용자 인증에 실패하였습니다. 다시 시도해주세요."),

    //COMMENT
    NOT_FOUND_COMMENT_CONTENT(3200, "댓글 내용은 반드시 입력되어야 합니다. 다시 한번 시도해주세요."),
    INVALID_COMMENT_CONTENT(3201, "댓글 내용은 50자를 초과할 수 없습니다. 다시 한번 시도해주세요."),
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
