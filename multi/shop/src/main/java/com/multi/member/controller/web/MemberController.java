package com.multi.member.controller.web;

import com.multi.config.auth.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String index(Model model) {
        SessionMember sessionMember = (SessionMember) session.getAttribute("LOGIN_SESSION_USER");
        if (sessionMember != null) {
            model.addAttribute("name", sessionMember.getName());
            model.addAttribute("email", sessionMember.getEmail());
        }
        return "member/loginForm";
    }
}
