package com.shoppingmall.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostSuccessCode implements SuccessCode {
    SUCCESS_SAVE_POST(HttpStatus.OK, "Success save post."),
    SUCCESS_DELETE_POST(HttpStatus.OK, "Success delete post."),
    SUCCESS_UPDATE_POST(HttpStatus.OK, "Success update post."),
    SUCCESS_SAVE_COMMENT(HttpStatus.OK, "Success save comment."),
    SUCCESS_DELETE_COMMENT(HttpStatus.OK, "Success delete comment."),
    SUCCESS_UPDATE_COMMENT(HttpStatus.OK, "Success update comment."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
