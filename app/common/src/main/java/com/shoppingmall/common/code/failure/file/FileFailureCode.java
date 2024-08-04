package com.shoppingmall.common.code.failure.file;

import com.shoppingmall.common.code.failure.FailureCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileFailureCode implements FailureCode {
    FAIL_SAVE_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다. 다시 시도해주세요."),
    FAIL_DOWNLOAD_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드에 실패했습니다. 다시 시도해주세요."),
    FAIL_UPLOAD_FILES(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업데이트에 실패했습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus status;
    private final String message;
}