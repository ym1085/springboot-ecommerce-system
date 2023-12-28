package com.shoppingmall.api;

import com.shoppingmall.common.*;
import com.shoppingmall.config.auth.jwt.JwtTokenProvider;
import com.shoppingmall.dto.request.JwtTokenDto;
import com.shoppingmall.dto.request.LoginRequestDto;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.dto.request.RefreshTokenDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.exception.MemberAccountNotFoundException;
import com.shoppingmall.service.MemberService;
import com.shoppingmall.service.RefreshTokenService;
import com.shoppingmall.utils.ResponseUtils;
import com.shoppingmall.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class MemberRestController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

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
    public JwtTokenDto login(
            @RequestBody @Valid LoginRequestDto loginRequestDto,
            BindingResult bindingResult) {

        // check binding errors
        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        // login
        JwtTokenDto jwtTokenDto = memberService.login(loginRequestDto);

        return jwtTokenDto;
    }

    /**
     * 삭제의 경우 추후 redis로 변경 필요
     * @param refreshTokenDto 로그아웃 시에 삭제 해야하는 refresh token
     */
    @DeleteMapping("/member/logout")
    public ResponseEntity<CommonResponse> logout(
            @RequestBody @Valid RefreshTokenDto refreshTokenDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        int responseCode = refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());// delete refresh Token from database
        boolean success = ResponseUtils.isSuccessResponseCode(responseCode);
        MessageCode messageCode = success ? SuccessCode.SUCCESS_DELETE_REFRESH_TOKEN : ErrorCode.FAIL_DELETE_REFRESH_TOKEN;

        return ApiUtils.success(
            messageCode.getCode(),
            messageCode.getMessage(),
            success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Access Token 만료시 refresh token 기반 재발급
     * @param refreshTokenDto
     * @return
     */
    @PostMapping("/member/reissue")
    public JwtTokenDto reissueRefreshToken(
            @RequestBody RefreshTokenDto refreshTokenDto,
            HttpServletRequest request) {

        JwtTokenDto jwtTokenDto = memberService.reissue(refreshTokenDto);

        return jwtTokenDto;
    }

    @GetMapping("/member/exists/{account}")
    public ResponseEntity<CommonResponse> checkDuplMemberAccount(@PathVariable("account") String account) {
        if (StringUtils.hasText(account)) {
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


