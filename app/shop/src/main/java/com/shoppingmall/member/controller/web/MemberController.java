package com.shoppingmall.member.controller.web;

import com.shoppingmall.config.auth.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final HttpSession session;

    @GetMapping(value = "/member/loginForm")
    public String login(Model model, Authentication authentication) {
        SessionMember sessionMember = (SessionMember) session.getAttribute("LOGIN_SESSION_USER");
        if (sessionMember != null) {
            model.addAttribute("name", sessionMember.getName());
            model.addAttribute("email", sessionMember.getEmail());
        }
        return "member/login_form";
    }

    @GetMapping(value = "/member/joinForm")
    public String join(Model model) {
        return "member/join_form";
    }

    @GetMapping(value = "/member/access-denied")
    public String memberAccessDenied(Model model) {
        log.debug("member access denied..");
        return "member/access_denied";
    }
}
