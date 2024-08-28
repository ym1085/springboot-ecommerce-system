package com.shoppingmall.security;

import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.constant.Role;
import com.shoppingmall.security.annotation.WithMockCustomUser;
import com.shoppingmall.vo.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

// https://tecoble.techcourse.co.kr/post/2020-09-30-spring-security-test
// https://jiwondev.tistory.com/270
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = new Member();
        member.setMemberId(1);
        member.setUserName(customUser.username());
        member.setPassword(customUser.password());
        member.setRole(Role.USER);
        member.setAccount("admin");

        PrincipalUserDetails principalUserDetails = new PrincipalUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principalUserDetails, null, principalUserDetails.getAuthorities());

        context.setAuthentication(authenticationToken);
        return context;
    }
}