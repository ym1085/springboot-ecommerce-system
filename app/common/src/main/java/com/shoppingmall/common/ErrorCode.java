package com.shoppingmall.common;

public enum ErrorCode implements MessageCode {
    // FAIL CODE
    FAIL(0, "Fail"),

    INVALID_PARAMETER(9999, "Invalid Request Data"),
    FAIL_SAVE_POST(2000, "Failed to register post. Please try again."),
    FAIL_UPDATE_POST(2001, "Failed to update post. Please try again."),
    FAIL_DELETE_POST(2002, "Failed to delete post. Please try again."),
    FAIL_SAVE_COMMENT(2003, "Failed to register comment. Please try again."),
    FAIL_DELETE_COMMENT(2004, "Failed to delete comment. Please try again."),
    FAIL_UPDATE_COMMENT(2005, "Failed to update comment. Please try again."),
    FAIL_UPDATE_POST_READ_COUNT(2006, "Failed to update post read count. Please try again."),

    FAIL_SAVE_MEMBER(2100, "Failed to sign up member. Please try again."),
    FAIL_CERT_EMAIL(2101, "Failed to certify email. Please try again."),
    FAIL_DUPL_MEMBER(2102, "Duplicate member. Please try again."),
    FAIL_CHECK_DUPL_MEMBER(2103, "Failed to check for duplicate ID. Please try again."),

    FAIL_UPDATE_FILES(2200, "Failed to update file."),
    FAIL_DELETE_FILES(2201, "Failed to delete file."),
    FAIL_DOWNLOAD_FILES(2202, "Failed to download file."),
    FAIL_SAVE_FILES(2203, "Failed to save file."),

    // VALIDATION CODE
    NOT_FOUND_POST_ID(3000, "Post ID does not exist. Please try again."),
    NOT_FOUND_POST_TITLE(3001, "Title must be entered. Please try again."),
    NOT_FOUND_POST_CONTENT(3002, "Content must be entered. Please try again."),
    NOT_FOUND_POST_FIXED_YN(3003, "Fixed post must be designated as (Y/N). Please try again."),
    INVALID_POST_TITLE(3004, "Title cannot exceed 20 characters. Please try again."),
    INVALID_POST_CONTENT(3005, "Content cannot exceed 1000 characters. Please try again."),

    NOT_FOUND_MEMBER_NAME(3100, "Name is a required field. Please try again."),
    NOT_FOUND_MEMBER_ACCOUNT(3101, "ID is a required field. Please try again."),
    NOT_FOUND_MEMBER_PASSWORD(3102, "Password is a required field. Please try again."),
    NOT_FOUND_MEMBER_EMAIL(3103, "Email is a required field. Please try again."),
    NOT_FOUND_MEMBER_PHONE(3104, "Phone number is a required field. Please try again."),
    NOT_FOUND_MEMBER_EMAIL_CERT_YN(3105, "Email certification has not been completed. Please try again."),
    NOT_FOUND_MEMBER_DUPL_ACCOUNT_CERT_YN(3106, "ID duplication certification has not been completed. Please try again."),
    NOT_FOUND_MEMBER_GENDER(3107, "Gender is a required field. Please try again."),
    NOT_FOUND_MEMBER_BIRTHDATE(3108, "ID duplication certification has not been completed. Please try again."),
    INVALID_MEMBER_NAME(3109, "Please enter a name of at least two characters and up to six characters. Please try again."),
    INVALID_MEMBER_ACCOUNT(3110, "Only IDs up to 30 characters are allowed. Please try again."),
    INVALID_MEMBER_PASSWORD(3111, "Please enter a password of at least 8 characters including English letters and special characters. Please try again."),
    INVALID_MEMBER_EMAIL(3112, "Invalid email format. Please try again."),
    INVALID_MEMBER_PHONE(3113, "Invalid phone number format. Please try again."),
    INVALID_MEMBER_EMAIL_CERT_YN(3114, "User certification failed. Please try again."),
    INVALID_MEMBER_DUPL_ACCOUNT_CERT_YN(3115, "User certification failed. Please try again."),

    NOT_FOUND_COMMENT_CONTENT(3200, "Comment content must be entered. Please try again."),
    INVALID_COMMENT_CONTENT(3201, "Comment content cannot exceed 50 characters. Please try again."),

    NOT_FOUND_POST_FILES(3300, "No attached files found. Please try again."),
    NOT_FOUND_POST_FILES_PATH(3301, "No file path for attached files found. Please try again.")
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