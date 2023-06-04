package com.multi.index.controller.web;

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
public class IndexController {

    private final HttpSession session;

    @GetMapping(value = "/")
    public String index(Model model) {
        SessionMember member = (SessionMember) session.getAttribute("LOGIN_SESSION_USER");
        if (member != null) {
            model.addAttribute("memberName", member.getName());
        }
        return "index/index";
    }
}
