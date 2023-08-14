package com.multi.mail.controller.api;

import com.multi.mail.dto.request.EmailRequestDto;
import com.multi.mail.service.EmailService;
import com.multi.posts.constant.ResponseCode;
import com.multi.posts.constant.StatusEnum;
import com.multi.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class EmailApiController {

    private final EmailService emailService;

    /**
     * 이메일 인증 코드 전송
     * @param emailRequestDto
     * @return
     */
    @PostMapping(value = "/email/verify-request")
    public ResponseEntity sendMessage(@RequestBody EmailRequestDto emailRequestDto) {
        log.info("emailRequestDto = {}", emailRequestDto.toString());
        ApiResponse message = null;
        if (StringUtils.isBlank(emailRequestDto.getEmail())) {
            message = new ApiResponse<>(
                    StatusEnum.COULD_NOT_FOUND_EMAIL.getStatusCode(),
                    StatusEnum.COULD_NOT_FOUND_EMAIL.getMessage()
            );
            return ResponseEntity.badRequest().body(message);
        }
        emailService.sendCodeToEmail(emailRequestDto.getEmail());
        return ResponseEntity.ok().body(new ApiResponse<>(
                StatusEnum.SUCCESS_CERT_EMAIL.getStatusCode(),
                StatusEnum.SUCCESS_CERT_EMAIL.getMessage()
        ));
    }

    /**
     * 이메일 인증 진행
     * @param email 이메일 인증을 원하는 사용자 이메일
     * @param authCode 이메일 인증 코드
     * @return
     */
    @GetMapping(value = "/email/verify")
    public ResponseEntity verifyEmail(@RequestParam("email") String email,
                                      @RequestParam("code") String authCode) {
        log.debug("EmailService.emailAuthKey = {}, autoCode = {}", EmailService.emailAuthKey, authCode);
        if (EmailService.emailAuthKey.equals(authCode)) {
            return ResponseEntity.ok().body(ResponseCode.SUCCESS.getResponseCode()); // 1
        } else {
            return ResponseEntity.badRequest().body(ResponseCode.FAIL.getResponseCode()); // 0
        }
    }
}
