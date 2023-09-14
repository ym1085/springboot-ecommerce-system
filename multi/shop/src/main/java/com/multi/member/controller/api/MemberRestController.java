package com.multi.member.controller.api;

import com.multi.common.utils.ErrorUtils;
import com.multi.common.utils.message.CommonResponse;
import com.multi.common.utils.message.MessageCode;
import com.multi.common.utils.message.ResponseFactory;
import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            List<String> errorMessage = ErrorUtils.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(MessageCode.FAIL_SAVE_MEMBER, errorMessage, HttpStatus.BAD_REQUEST);
        }

        int result = memberService.signUp(memberRequestDto);
        return ResponseFactory.handlerResponseFactory(result, MessageCode.SUCCESS_SAVE_MEMBER, MessageCode.FAIL_SAVE_MEMBER);
    }
}
