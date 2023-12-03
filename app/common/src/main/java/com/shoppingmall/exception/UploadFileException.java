package com.shoppingmall.exception;

import com.shoppingmall.common.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// https://recordsoflife.tistory.com/501
@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UploadFileException extends RuntimeException {

    private final MessageCode messageCode;

}
