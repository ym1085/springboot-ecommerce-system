package com.shoppingmall.common.code.success.file;

import com.shoppingmall.common.code.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileSuccessCode implements SuccessCode {
    SUCCESS_SAVE_FILES(HttpStatus.OK, "파일 업로드에 성공하였습니다."),
    SUCCESS_UPDATE_FILES(HttpStatus.OK, "파일 수정에 성공하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}