package com.shoppingmall.api;

import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.MessageCode;
import com.shoppingmall.common.ResponseFactory;
import com.shoppingmall.dto.request.EmailRequestDto;
import com.shoppingmall.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class EmailRestController {

    private final EmailService emailService;

    @PostMapping(value = "/email/verify-request")
    public ResponseEntity<CommonResponse> sendMessage(
            @RequestBody EmailRequestDto emailRequestDto) {

        if (StringUtils.isBlank(emailRequestDto.getEmail())) {
            return ResponseFactory.createResponseFactory(MessageCode.NOT_FOUND_EMAIL.getCode(), MessageCode.NOT_FOUND_EMAIL.getMessage(), HttpStatus.BAD_REQUEST);
        }

        emailService.sendAuthCodeToMemberEmail(emailRequestDto.getEmail());

        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_SEND_CODE.getCode(), MessageCode.SUCCESS_SEND_CODE.getMessage(), HttpStatus.OK);
    }

    @GetMapping(value = "/email/verify")
    public ResponseEntity<CommonResponse> verifyEmail(
            @RequestParam("email") String email,
            @RequestParam("code") String authCode) {

        if (!EmailService.emailAuthKey.equals(authCode)) {
            return ResponseFactory.createResponseFactory(MessageCode.FAIL_CERT_EMAIL.getCode(), MessageCode.FAIL_CERT_EMAIL.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_CERT_EMAIL.getCode(), MessageCode.SUCCESS_CERT_EMAIL.getMessage(), HttpStatus.OK);
    }
}
