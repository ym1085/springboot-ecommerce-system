package com.shoppingmall.common.code.failure.post;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostFailureCode implements FailureCode {
    NOT_FOUND_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글이 존재하지 않습니다. 다시 시도해주세요."),
    FAIL_SAVE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 수정에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 삭제에 실패했습니다. 다시 시도해주세요."),
    FAIL_SAVE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 댓글 등록에 실패했습니다. 다시 시도해주세요."),
    FAIL_DELETE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 댓글 삭제에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPDATE_COMMENT(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 댓글 수정에 실패했습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus status;
    private final String message;
}