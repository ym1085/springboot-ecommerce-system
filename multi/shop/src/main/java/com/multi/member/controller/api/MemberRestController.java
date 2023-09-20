package com.multi.member.controller.api;

import com.multi.common.utils.BindingResultErrorUtils;
import com.multi.common.utils.message.CommonResponse;
import com.multi.common.utils.message.MessageCode;
import com.multi.common.utils.message.ResponseFactory;
import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
            List<String> errorMessage = BindingResultErrorUtils.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(MessageCode.FAIL_SAVE_MEMBER.getCode(), errorMessage, HttpStatus.BAD_REQUEST);
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
            return ResponseFactory.createResponseFactory(MessageCode.NOT_FOUND_ACCOUNT.getCode(), MessageCode.NOT_FOUND_ACCOUNT.getMessage(), HttpStatus.OK);
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
