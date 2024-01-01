package com.shoppingmall.common;

public enum SuccessCode implements MessageCode {
    // 성공 코드
    SUCCESS(1, "성공"),

    SUCCESS_GET_POSTS(1000, "모든 게시물을 성공적으로 조회했습니다."),
    SUCCESS_GET_POST(1001, "단일 게시물을 성공적으로 조회했습니다."),
    SUCCESS_SAVE_POST(1002, "게시물을 성공적으로 등록했습니다."),
    SUCCESS_UPDATE_POST(1003, "게시물을 성공적으로 수정했습니다."),
    SUCCESS_DELETE_POST(1004, "게시물을 성공적으로 삭제했습니다."),

    SUCCESS_SAVE_MEMBER(1100, "회원 가입을 성공적으로 완료했습니다."),
    SUCCESS_SEND_EMAIL(1101, "이메일 인증 코드를 성공적으로 전송했습니다."),
    SUCCESS_CERT_EMAIL(1102, "이메일을 성공적으로 인증했습니다."),
    SUCCESS_DUPL_ACCOUNT(1103, "사용 가능한 계정입니다."),

    SUCCESS_SAVE_COMMENT(1200, "댓글을 성공적으로 등록했습니다."),
    SUCCESS_DELETE_COMMENT(1201, "댓글을 성공적으로 삭제했습니다."),
    SUCCESS_UPDATE_COMMENT(1202, "댓글을 성공적으로 수정했습니다."),

    SUCCESS_UPDATE_FILES(1300, "파일을 성공적으로 업데이트했습니다."),
    SUCCESS_DELETE_FILES(1301, "파일을 성공적으로 삭제했습니다."),
    SUCCESS_DOWNLOAD_FILES(1302, "파일을 성공적으로 다운로드했습니다."),
    SUCCESS_SAVE_FILES(1303, "파일을 성공적으로 저장했습니다."),
    SUCCESS_GZIP_DOWNLOAD_FILES(1304, "gzip 파일을 성공적으로 다운로드했습니다."),

    SUCCESS_LOGOUT_MEMBER(1400, "로그아웃에 성공하였습니다.")
    ;

    private final int code;
    private final String message;

    SuccessCode(int code, String message) {
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
