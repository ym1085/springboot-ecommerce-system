package com.post.constant;

public enum StatusEnum {
    // Basic HTTP Status Code
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    // Success code and message
    SUCCESS_GET_POSTS(200, "전체 게시글 조회에 성공 하였습니다."),
    SUCCESS_GET_POST(200, "단일 게시글 조회에 성공 하였습니다."),
    SUCCESS_SAVE_POST(200, "게시글 등록에 성공 하였습니다."),
    SUCCESS_UPDATE_POST(200, "게시글 수정에 성공 하였습니다."),
    SUCCESS_DELETE_POST(200, "게시글 삭제에 성공 하였습니다."),
    SUCCESS_SAVE_COMMENT(200, "댓글 등록에 성공 하였습니다."),
    SUCCESS_DELETE_COMMENT(200, "댓글 삭제에 성공 하였습니다."),
    SUCCESS_UPDATE_COMMENT(200, "댓글 수정에 성공 하였습니다."),

    // Fail code and message
    COULD_NOT_FOUND_POST_ID(400, "게시글 번호가 존재하지 않습니다. 다시 시도해주세요."),
    COULD_NOT_SAVE_POST(500, "게시글 등록에 실패하였습니다. 다시 시도해주세요."),
    COULD_NOT_SAVE_COMMENT(500, "댓글 등록에 실패하였습니다. 다시 시도해주세요."),
    COULD_NOT_DELETE_COMMENT(500, "댓글 삭제에 실패하였습니다. 다시 시도해주세요."),
    COULD_NOT_UPDATE_COMMENT(500, "댓글 수정에 실패하였습니다. 다시 시도해주세요.")
    ;

    private final int statusCode;
    private final String message;

    StatusEnum(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
