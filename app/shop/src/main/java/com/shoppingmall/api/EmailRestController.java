package com.shoppingmall.api;

import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.dto.request.EmailRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.exception.MemberException;
import com.shoppingmall.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.shoppingmall.common.code.failure.member.MemberFailureCode.AUTHENTICATION_MEMBER_EMAIL;
import static com.shoppingmall.common.code.success.member.MemberSuccessCode.SEND_AUTH_EMAIL;
import static com.shoppingmall.common.code.success.member.MemberSuccessCode.VERIFY_AUTH_EMAIL;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class EmailRestController {

    private final EmailService emailService;

    @PostMapping("/email/verify-request")
    public ResponseEntity<BaseResponse<?>> sendMessage(
            @RequestBody EmailRequestDto emailRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }
        emailService.sendAuthCodeToMemberEmail(emailRequestDto.getEmail());
        return ApiResponseUtils.success(SEND_AUTH_EMAIL);
    }

    @GetMapping("/email/verify")
    public ResponseEntity<BaseResponse<?>> verifyEmail(
            @RequestParam("code") String authCode) {

        if (!EmailService.emailAuthKey.equals(authCode)) {
            log.error(AUTHENTICATION_MEMBER_EMAIL.getMessage());
            throw new MemberException(AUTHENTICATION_MEMBER_EMAIL);
        }
        return ApiResponseUtils.success(VERIFY_AUTH_EMAIL);
    }
}
