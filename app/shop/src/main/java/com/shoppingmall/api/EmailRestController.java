package com.shoppingmall.api;

import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.dto.request.EmailRequestDto;
import com.shoppingmall.exception.MemberException;
import com.shoppingmall.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.shoppingmall.common.code.failure.member.MemberFailureCode.AUTHENTICATION_MEMBER_EMAIL;
import static com.shoppingmall.common.code.success.member.MemberSuccessCode.SUCCESS_SEND_AUTH_EMAIL;
import static com.shoppingmall.common.code.success.member.MemberSuccessCode.SUCCESS_VERIFY_AUTH_EMAIL;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class EmailRestController {

    private final EmailService emailService;

    @PostMapping("/email/send")
    public ResponseEntity<BaseResponse<?>> sendMail(
            @RequestBody EmailRequestDto emailRequestDto) {

        if (emailRequestDto == null || !StringUtils.hasText(emailRequestDto.getEmail())) {
            log.error("Invalid parameters error, when sending email to user");
            throw new MemberException(AUTHENTICATION_MEMBER_EMAIL);
        }

        emailService.sendEmail(emailRequestDto.getEmail());
        return ApiResponseUtils.success(SUCCESS_SEND_AUTH_EMAIL);
    }

    @GetMapping("/email/verify")
    public ResponseEntity<BaseResponse<?>> verifyEmail(
            @ModelAttribute EmailRequestDto emailRequestDto) {

        if (emailRequestDto == null
                || !StringUtils.hasText(emailRequestDto.getEmail())
                || !StringUtils.hasText(emailRequestDto.getVerifyCode())) {
            log.error("Invalid parameters error, when authenticating user email auth");
            throw new MemberException(AUTHENTICATION_MEMBER_EMAIL);
        }

        // email 별 auth token은 3분이 지나면 자동 소멸, 삭제할 필요는 없음
        Boolean emailAuthResult = emailService.verifyEmail(emailRequestDto.getEmail(), emailRequestDto.getVerifyCode());
        if (!emailAuthResult) {
            log.error(AUTHENTICATION_MEMBER_EMAIL.getMessage());
            throw new MemberException(AUTHENTICATION_MEMBER_EMAIL);
        }
        return ApiResponseUtils.success(SUCCESS_VERIFY_AUTH_EMAIL);
    }
}
