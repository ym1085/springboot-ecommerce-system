package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.success.MemberSuccessCode;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/member/join")
    public ResponseEntity<CommonResponse> join(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        int responseCode = memberService.join(memberRequestDto);

        return ApiUtils.success(
                MemberSuccessCode.SUCCESS_JOIN_MEMBER.getHttpStatus(),
                MemberSuccessCode.SUCCESS_JOIN_MEMBER.getMessage()
        );
    }

    @PostMapping("/member/exists")
    public ResponseEntity<CommonResponse> checkDuplicateMemberAccount(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        memberService.validateDuplicateMemberAccount(memberRequestDto.getAccount());

        return ApiUtils.success(
                MemberSuccessCode.NONE_DUPLICATE_MEMBER.getHttpStatus(),
                MemberSuccessCode.NONE_DUPLICATE_MEMBER.getMessage()
        );
    }
}


