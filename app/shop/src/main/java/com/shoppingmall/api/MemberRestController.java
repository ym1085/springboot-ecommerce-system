package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
            @RequestBody @Valid MemberSaveRequestDto memberRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        memberService.join(memberRequestDto);

        return ApiUtils.success(
                SuccessCode.SUCCESS_JOIN_MEMBER.getHttpStatus(),
                SuccessCode.SUCCESS_JOIN_MEMBER.getMessage()
        );
    }

    @PostMapping("/member/exists")
    public ResponseEntity<CommonResponse> checkDuplicateMemberAccount(
            @RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        if (!StringUtils.hasText(memberSaveRequestDto.getAccount())) {
            throw new IllegalArgumentException();
        }

        memberService.validateDuplicateMemberAccount(memberSaveRequestDto.getAccount());

        return ApiUtils.success(
                SuccessCode.NONE_DUPLICATE_MEMBER.getHttpStatus(),
                SuccessCode.NONE_DUPLICATE_MEMBER.getMessage()
        );
    }
}


