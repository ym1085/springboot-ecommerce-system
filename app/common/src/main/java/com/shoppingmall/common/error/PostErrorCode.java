package com.shoppingmall.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    FAIL_SAVE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register the post. Please try again."),
    FAIL_UPDATE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update the post. Please try again."),
    FAIL_DELETE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete the post. Please try again."),
    FAIL_SAVE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register the comment. Please try again."),
    FAIL_DELETE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete the comment. Please try again."),
    FAIL_UPDATE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update the comment. Please try again."),
    FAIL_SAVE_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload the post file. Please try again."),
    FAIL_DOWNLOAD_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download the post file. Please try again."),
    FAIL_UPLOAD_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update the post file. Please try again."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
