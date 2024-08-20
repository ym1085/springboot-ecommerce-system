package com.shoppingmall.web;

import com.shoppingmall.config.auth.attribute.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final HttpSession session;

    /**
     * 로그인 모달 폼 반환
     */
    @GetMapping("/loginForm")
    public String loginForm(Model model, Authentication authentication) {
        SessionMember sessionMember = (SessionMember) session.getAttribute("LOGIN_SESSION_USER");
        if (sessionMember != null) { // 로그인 했을 경우 로그인 페이지에 name, email 같이 전달 (일반 사용자, 소셜 로그인 사용자)
            model.addAttribute("name", sessionMember.getName());
            model.addAttribute("email", sessionMember.getEmail());
        }
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
