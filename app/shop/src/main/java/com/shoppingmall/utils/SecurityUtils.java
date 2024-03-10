package com.shoppingmall.utils;

import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.vo.Member;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@NoArgsConstructor
public class SecurityUtils {

    // SecurityContext에 유저 정보가 저장되는 시점
    public static Optional<Member> getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof PrincipalUserDetails)) {
            return Optional.empty();
        }

        PrincipalUserDetails principalUserDetails = (PrincipalUserDetails) authentication.getPrincipal();
        Member member = principalUserDetails.getMember();
        return Optional.ofNullable(member);
    }

}
