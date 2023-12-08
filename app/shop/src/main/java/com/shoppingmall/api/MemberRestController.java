package com.shoppingmall.api;

import com.shoppingmall.common.*;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.exception.MemberAccountNotFoundException;
import com.shoppingmall.service.MemberService;
import com.shoppingmall.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping(value = "/member/join")
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

    @GetMapping(value = "/member/exists/{account}")
    public ResponseEntity<CommonResponse> checkDuplMemberAccount(@PathVariable("account") String account) {
        if (StringUtils.isBlank(account)) {
            throw new MemberAccountNotFoundException();
        }

        int responseCode = memberService.checkDuplMemberAccount(MemberRequestDto.builder().account(account).build());
        boolean success = ResponseUtils.isSuccessResponseCode(responseCode);
        MessageCode messageCode = success ? SuccessCode.SUCCESS_DUPL_ACCOUNT : ErrorCode.FAIL_DUPL_MEMBER;

        return ApiUtils.success(
                messageCode.getCode(),
                messageCode.getMessage(),
                success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
