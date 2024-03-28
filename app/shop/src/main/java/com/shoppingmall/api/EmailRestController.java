package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.EmailRequestDto;
import com.shoppingmall.exception.FailAuthenticationMemberEmailException;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class EmailRestController {

    private final EmailService emailService;

    @PostMapping("/email/verify-request")
    public ResponseEntity<CommonResponse> sendMessage(
            @RequestBody EmailRequestDto emailRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        emailService.sendAuthCodeToMemberEmail(emailRequestDto.getEmail());

        return ApiUtils.success(SuccessCode.SEND_AUTH_EMAIL.getCode(), SuccessCode.SEND_AUTH_EMAIL.getHttpStatus(), SuccessCode.SEND_AUTH_EMAIL.getMessage());
    }

    @GetMapping("/email/verify")
    public ResponseEntity<CommonResponse> verifyEmail(
            @RequestParam("code") String authCode) {

        if (!EmailService.emailAuthKey.equals(authCode)) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.AUTHENTICATION_MEMBER_EMAIL.getMessage());
            throw new FailAuthenticationMemberEmailException(ErrorCode.AUTHENTICATION_MEMBER_EMAIL);
        }

        return ApiUtils.success(SuccessCode.VERIFY_AUTH_EMAIL.getCode(), SuccessCode.VERIFY_AUTH_EMAIL.getHttpStatus(), SuccessCode.VERIFY_AUTH_EMAIL.getMessage());
    }
}
