package com.shoppingmall.api;

import com.shoppingmall.common.*;
import com.shoppingmall.dto.request.EmailRequestDto;
import com.shoppingmall.exception.EmailNotFoundException;
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

    @PostMapping("/email/verify-request")
    public ResponseEntity<CommonResponse> sendMessage(
            @RequestBody EmailRequestDto emailRequestDto) {

        if (StringUtils.isBlank(emailRequestDto.getEmail())) {
            throw new EmailNotFoundException();
        }

        emailService.sendAuthCodeToMemberEmail(emailRequestDto.getEmail());

        return ApiUtils.success(SuccessCode.SUCCESS_SEND_EMAIL.getCode(), SuccessCode.SUCCESS_SEND_EMAIL.getMessage(), HttpStatus.OK);
    }

    @GetMapping("/email/verify")
    public ResponseEntity<CommonResponse> verifyEmail(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam("code") String authCode) {

        if (!EmailService.emailAuthKey.equals(authCode)) {
            return ApiUtils.fail(ErrorCode.FAIL_CERT_EMAIL.getCode(), ErrorCode.FAIL_CERT_EMAIL.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ApiUtils.success(SuccessCode.SUCCESS_CERT_EMAIL.getCode(), SuccessCode.SUCCESS_CERT_EMAIL.getMessage(), HttpStatus.OK);
    }
}
