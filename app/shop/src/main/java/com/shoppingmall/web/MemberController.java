package com.shoppingmall.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    /**
     * 로그인 모달 폼 반환
     */
    @GetMapping("/loginForm")
    public String loginForm() {
        return "member/loginForm";
    }

    /**
     * 회원가입 모달 폼 반환
     */
    @GetMapping("/joinForm")
    public String joinForm() {
        return "member/joinForm";
    }
}
