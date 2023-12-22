package com.shoppingmall.api;

import com.shoppingmall.common.*;
import com.shoppingmall.config.jwt.dto.request.LoginRequestDto;
import com.shoppingmall.config.jwt.dto.request.JwtToken;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.exception.MemberAccountNotFoundException;
import com.shoppingmall.service.MemberService;
import com.shoppingmall.utils.ResponseUtils;
import com.shoppingmall.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/member/join")
    public ResponseEntity<CommonResponse> join(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        int responseCode = memberService.join(memberRequestDto);
        boolean success = ResponseUtils.isSuccessResponseCode(responseCode);
        MessageCode messageCode = success ? SuccessCode.SUCCESS_SAVE_MEMBER : ErrorCode.FAIL_SAVE_MEMBER;

        return ApiUtils.success(
                messageCode.getCode(),
                messageCode.getMessage(),
                success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/member/login/authentication")
    public ResponseEntity<?> authenticate(@RequestHeader Map<String, String> requestHeader) {
        log.info("requestHeader = {}", requestHeader.toString());
        return new ResponseEntity<>(SecurityUtils.getCurrentMemberId(), HttpStatus.OK);
    }

    @PostMapping("/member/login")
    public JwtToken login(
            @RequestBody @Valid LoginRequestDto loginRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        log.info("username = {}, password = {}", username, password);

        JwtToken jwtToken = memberService.login(username, password);
        log.debug("jwtToken = {}", jwtToken);

        return jwtToken;
    }

    @GetMapping("/member/exists/{account}")
    public ResponseEntity<CommonResponse> checkDuplMemberAccount(@PathVariable("account") String account) {
        if (StringUtils.isBlank(account)) {
            throw new MemberAccountNotFoundException();
        }

        int responseCode = memberService.checkDuplMemberAccount(account);
        boolean success = ResponseUtils.isSuccessResponseCode(responseCode);
        MessageCode messageCode = success ? SuccessCode.SUCCESS_DUPL_ACCOUNT : ErrorCode.FAIL_DUPL_MEMBER;

        return ApiUtils.success(
                messageCode.getCode(),
                messageCode.getMessage(),
                success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}


