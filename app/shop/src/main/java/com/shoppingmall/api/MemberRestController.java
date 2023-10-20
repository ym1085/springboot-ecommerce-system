package com.shoppingmall.api;

import com.shoppingmall.common.BindingResultError;
import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.MessageCode;
import com.shoppingmall.common.ResponseFactory;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.service.MemberService;
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

    @PostMapping(value = "/member/join")
    public ResponseEntity<CommonResponse> join(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultError.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(
                    MessageCode.FAIL_SAVE_MEMBER.getCode(),
                    MessageCode.FAIL_SAVE_MEMBER.getMessage(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST
            );
        }

        int result = memberService.signUp(memberRequestDto);
        return ResponseFactory.handlerResponseFactory(
                result,
                MessageCode.SUCCESS_SAVE_MEMBER,
                (result == 0) ? MessageCode.FAIL_DUPL_MEMBER : MessageCode.FAIL_SAVE_MEMBER
        );
    }

    @GetMapping(value = "/member/exists/{account}")
    public ResponseEntity<CommonResponse> checkDuplMemberAccount(@PathVariable("account") String account) {
        if (StringUtils.isBlank(account)) {
            return ResponseFactory.createResponseFactory(
                    MessageCode.NOT_FOUND_ACCOUNT.getCode(),
                    MessageCode.NOT_FOUND_ACCOUNT.getMessage(),
                    HttpStatus.OK
            );
        }

        int result = memberService.checkDuplMemberAccount(MemberRequestDto.builder().account(account).build());

        MessageCode messageCode = (result == 1) ? MessageCode.FAIL_DUPL_MEMBER : MessageCode.SUCCESS_DUPL_ACCOUNT;
        return ResponseFactory.createResponseFactory(
                messageCode.getCode(),
                messageCode.getMessage(),
                HttpStatus.OK
        );
    }
}
