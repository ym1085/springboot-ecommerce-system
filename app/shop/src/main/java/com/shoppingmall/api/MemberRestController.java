package com.shoppingmall.api;

import com.shoppingmall.common.ApiUtils;
import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.SuccessCode;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.exception.MemberAccountNotFoundException;
import com.shoppingmall.service.MemberService;
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
        return ApiUtils.success(
                SuccessCode.SUCCESS_SAVE_MEMBER.getCode(),
                SuccessCode.SUCCESS_SAVE_MEMBER.getMessage(),
                responseCode == 1 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping(value = "/member/exists/{account}")
    public ResponseEntity<CommonResponse> checkDuplMemberAccount(@PathVariable("account") String account) {
        if (StringUtils.isBlank(account)) {
            throw new MemberAccountNotFoundException();
        }

        int responseCode = memberService.checkDuplMemberAccount(MemberRequestDto.builder().account(account).build());
        return ApiUtils.success(
                SuccessCode.SUCCESS_DUPL_ACCOUNT.getCode(),
                SuccessCode.SUCCESS_DUPL_ACCOUNT.getMessage(),
                responseCode == 1 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
