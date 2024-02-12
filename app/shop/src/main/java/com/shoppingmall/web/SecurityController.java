package com.shoppingmall.web;

import com.shoppingmall.config.auth.PrincipalUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class SecurityController {

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal UserDetails userDetails) { // DI(authentication)

        log.info("requestURI = /test/login");
        PrincipalUserDetails principalUserDetails = (PrincipalUserDetails) authentication.getPrincipal();
        log.info("principalDetails = {}", principalUserDetails.getMember());
        log.info("userDetails = {}", userDetails.getUsername());

        return "세션 정보 확인";
    }

    @GetMapping("/test/oauth2/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {

        log.info("requestURI = /test/oauth/login");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("oAuth2User = {}", oAuth2User.getAttributes());
        log.info("oauth = {}", oauth.getAttributes());

        return "OAuth2 세션 정보 확인";
    }

    /*@GetMapping({"/", ""})
    public @ResponseBody String index() {
        return "index";
    }*/

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        log.info("principalDetails = {}", principalUserDetails.getAttributes());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
