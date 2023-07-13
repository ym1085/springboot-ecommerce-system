package com.multi.member.controller.api;

import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.dto.response.MemberResponseDto;
import com.multi.member.service.MemberService;
import com.multi.posts.constant.StatusEnum;
import com.multi.utils.ApiResponse;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 가입
     * @param memberRequestDto 사용자 회원 가입 정보
     * @description 회원가입의 경우 SNS 연동을 통한 로그인이 아닌 일반 로그인을 원하는 유저가 있다 가정하고 구현
     */
    @PostMapping(value = "/member/join")
    public ResponseEntity join(@RequestBody @Valid MemberRequestDto memberRequestDto,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            ApiResponse<String> message = new ApiResponse<>(StatusEnum.BAD_REQUEST, errorMessage);
            return ResponseEntity.badRequest().body(message);
        }

        MemberResponseDto memberResponseDto = memberService.signUp(memberRequestDto);
        Long successId = (memberResponseDto.getMemberId() != null) ? 1L : 0L;
        ApiResponse<Long> message;
        HttpStatus status;
        if (successId > 0) {
            message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_SAVE_MEMBER.getMessage(), successId);
            status = HttpStatus.OK;
        } else {
            message = new ApiResponse<>(StatusEnum.INTERNAL_SERVER_ERROR, StatusEnum.COULD_NOT_SAVE_MEMBER.getMessage(), successId);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(message);
    }
}
