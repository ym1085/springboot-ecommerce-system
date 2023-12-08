package com.shoppingmall.common;

public enum SuccessCode implements MessageCode {
    // SUCCESS CODE
    SUCCESS(1, "Success"),

    SUCCESS_GET_POSTS(1000, "Successfully retrieved all posts."),
    SUCCESS_GET_POST(1001, "Successfully retrieved a single post."),
    SUCCESS_SAVE_POST(1002, "Successfully registered a post."),
    SUCCESS_UPDATE_POST(1003, "Successfully updated a post."),
    SUCCESS_DELETE_POST(1004, "Successfully deleted a post."),

    SUCCESS_SAVE_MEMBER(1100, "Successfully signed up member."),
    SUCCESS_SEND_EMAIL(1101, "Successfully sent email verification code."),
    SUCCESS_CERT_EMAIL(1102, "Successfully verified email."),
    SUCCESS_DUPL_ACCOUNT(1103, "Account available."),

    SUCCESS_SAVE_COMMENT(1200, "Successfully registered a comment."),
    SUCCESS_DELETE_COMMENT(1201, "Successfully deleted a comment."),
    SUCCESS_UPDATE_COMMENT(1202, "Successfully updated a comment."),

    SUCCESS_UPDATE_FILES(1300, "Successfully updated files."),
    SUCCESS_DELETE_FILES(1301, "Successfully deleted files."),
    SUCCESS_DOWNLOAD_FILES(1302, "Successfully downloaded files."),
    SUCCESS_SAVE_FILES(1303, "Successfully saved files."),
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
