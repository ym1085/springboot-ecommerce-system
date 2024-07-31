package com.shoppingmall.config.auth;

import com.shoppingmall.config.auth.attribute.SessionMember;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

// 시큐리티 설정에서 loginProcessingUrl("/member/login");
// login 요청이 오면 자동으로 UserDetailService 타입으로 IoC 되어 있는 loadUserByUsername 호출

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalUserDetailsService implements UserDetailsService {
    private static final String SESSION_NAME = "LOGIN_SESSION_USER";
    private final MemberMapper memberMapper;
    private final HttpSession session; // JWT 사용하게 되면 SESSION 사용은 필요없어짐

    // 001. Security Session > Authentication > UserDetails
    // 002. Security Session > Authentication(UserDetails)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberMapper.getMemberByAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다, username = " + username));

        SessionMember sessionMember = new SessionMember(member);
        session.setAttribute(SESSION_NAME, sessionMember);

        return new PrincipalUserDetails(member);
    }
}
