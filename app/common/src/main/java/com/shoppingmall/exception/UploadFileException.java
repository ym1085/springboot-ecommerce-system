package com.shoppingmall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// https://recordsoflife.tistory.com/501
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UploadFileException extends RuntimeException {

    public UploadFileException(String msg) {
        super(msg);
    }
}
