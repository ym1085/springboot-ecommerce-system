package com.shoppingmall.utils;

import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.vo.Member;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        Member member = principalUserDetails.getLoginMember();
        return Optional.ofNullable(member);
    }

    public static boolean isValidLoginMember(PrincipalUserDetails principalUserDetails) {
        Member member = null;
        if (principalUserDetails != null) {
            member = principalUserDetails.getMember();
        }
        return member != null && member.getMemberId() != null && member.getMemberId() != 0 && StringUtils.isNotBlank(member.getAccount());
    }
}
