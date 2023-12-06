package com.shoppingmall.config.auth;

import com.shoppingmall.domain.Member;
import com.shoppingmall.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/member/login");
// login 요청이 오면 자동으로 UserDetailService 타입으로 IoC 되어 있는 loadUserByUsername 호출

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    // Security Session > Authentication > UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("LoginInfo - username = {}", username);
        Member member = memberMapper.getMemberByAccount(username).orElse(new Member());
        if (member != null) {
            return new PrincipalDetails(member);
        }
        return null;
    }
}
