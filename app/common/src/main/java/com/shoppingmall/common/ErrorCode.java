package com.shoppingmall.common;

// Fixme: ErrorCode 뜯어 고쳐야함
public enum ErrorCode implements MessageCode {
    // 실패 코드
    FAIL(0, "실패"),

    INVALID_PARAMETER(9999, "유효하지 않은 요청 데이터"),
    FAIL_SAVE_POST(2000, "게시물 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST(2001, "게시물 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_POST(2002, "게시물 삭제에 실패했습니다. 다시 시도해주세요."),
    FAIL_SAVE_COMMENT(2003, "댓글 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_COMMENT(2004, "댓글 삭제에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_COMMENT(2005, "댓글 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST_READ_COUNT(2006, "게시물 조회수 업데이트에 실패했습니다. 다시 시도해주세요."),

    FAIL_SAVE_MEMBER(2100, "회원 가입에 실패했습니다. 다시 시도해주세요."),
    FAIL_CERT_EMAIL(2101, "이메일 인증에 실패했습니다. 다시 시도해주세요."),
    FAIL_DUPL_MEMBER(2102, "중복된 회원입니다. 다시 시도해주세요."),
    FAIL_CHECK_DUPL_MEMBER(2103, "중복 아이디 확인에 실패했습니다. 다시 시도해주세요."),

    FAIL_UPDATE_FILES(2200, "파일 업데이트에 실패했습니다."),
    FAIL_DELETE_FILES(2201, "파일 삭제에 실패했습니다."),
    FAIL_DOWNLOAD_FILES(2202, "파일 다운로드에 실패했습니다."),
    FAIL_SAVE_FILES(2203, "파일 저장에 실패했습니다."),

    FAIL_DELETE_REFRESH_TOKEN(2300, "JWT Refresh Token 삭제 실패."),

    // 유효성 검사 코드
    NOT_FOUND_POST_ID(3000, "게시물 ID가 존재하지 않습니다. 다시 시도해주세요."),
    NOT_FOUND_POST_TITLE(3001, "제목을 입력해야 합니다. 다시 시도해주세요."),
    NOT_FOUND_POST_CONTENT(3002, "내용을 입력해야 합니다. 다시 시도해주세요."),
    NOT_FOUND_POST_FIXED_YN(3003, "고정 게시물 여부를 지정해야 합니다 (Y/N). 다시 시도해주세요."),
    INVALID_POST_TITLE(3004, "제목은 20자를 초과할 수 없습니다. 다시 시도해주세요."),
    INVALID_POST_CONTENT(3005, "내용은 1000자를 초과할 수 없습니다. 다시 시도해주세요."),

    NOT_FOUND_MEMBER_NAME(3100, "이름은 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_ACCOUNT(3101, "아이디는 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PASSWORD(3102, "비밀번호는 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_EMAIL(3103, "이메일은 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_PHONE(3104, "전화번호는 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_EMAIL_CERT_YN(3105, "이메일 인증이 완료되지 않았습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_DUPL_ACCOUNT_CERT_YN(3106, "아이디 중복 확인이 완료되지 않았습니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_GENDER(3107, "성별은 필수 입력 항목입니다. 다시 시도해주세요."),
    NOT_FOUND_MEMBER_BIRTHDATE(3108, "생년월일이 입력되지 않았습니다. 다시 시도해주세요."),
    INVALID_MEMBER_NAME(3109, "이름은 최소 두 자 이상, 여섯 자를 초과할 수 없습니다. 다시 시도해주세요."),
    INVALID_MEMBER_ACCOUNT(3110, "아이디는 최대 30자까지만 허용됩니다. 다시 시도해주세요."),
    INVALID_MEMBER_PASSWORD(3111, "영문 문자와 특수 문자를 포함한 최소 8자의 비밀번호를 입력해주세요. 다시 시도해주세요."),
    INVALID_MEMBER_EMAIL(3112, "유효하지 않은 이메일 형식입니다. 다시 시도해주세요."),
    INVALID_MEMBER_PHONE(3113, "유효하지 않은 전화번호 형식입니다. 다시 시도해주세요."),
    INVALID_MEMBER_EMAIL_CERT_YN(3114, "사용자 인증에 실패했습니다. 다시 시도해주세요."),
    INVALID_MEMBER_DUPL_ACCOUNT_CERT_YN(3115, "사용자 인증에 실패했습니다. 다시 시도해주세요."),
    NOT_MATCHED_MEMBER_PASSWORD(3116, "비밀번호가 일치하지 않습니다. 다시 시도해주세요"),

    NOT_FOUND_COMMENT_CONTENT(3200, "댓글 내용을 입력해야 합니다. 다시 시도해주세요."),
    INVALID_COMMENT_CONTENT(3201, "댓글 내용은 50자를 초과할 수 없습니다. 다시 시도해주세요."),

    NOT_FOUND_POST_FILES(3300, "첨부된 파일이 없습니다. 다시 시도해주세요."),
    NOT_FOUND_POST_FILES_PATH(3301, "첨부된 파일 경로가 없습니다. 다시 시도해주세요.")
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
