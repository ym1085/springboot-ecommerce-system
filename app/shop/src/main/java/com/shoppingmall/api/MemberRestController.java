package com.shoppingmall.api;

import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.shoppingmall.common.code.success.member.MemberSuccessCode.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/member/join")
    public ResponseEntity<BaseResponse<?>> join(
            @RequestBody @Valid MemberSaveRequestDto memberRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }
        memberService.join(memberRequestDto);
        return ApiResponseUtils.success(SAVE_MEMBER);
    }

    @PostMapping("/member/exists")
    public ResponseEntity<BaseResponse<?>> checkDuplicateMemberAccount(
            @RequestBody MemberSaveRequestDto memberSaveRequestDto) {

        if (!StringUtils.hasText(memberSaveRequestDto.getAccount())) {
            throw new IllegalArgumentException();
        }
        memberService.validateDuplicateMemberAccount(memberSaveRequestDto.getAccount());
        return ApiResponseUtils.success(NONE_DUPLICATE_MEMBER);
    }
}


