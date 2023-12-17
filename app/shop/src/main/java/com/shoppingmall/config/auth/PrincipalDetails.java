package com.shoppingmall.config.auth;

import com.shoppingmall.domain.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

// 시큐리티가 -> /member/login 주소 요청이 오면 낚아채서 로그인 진행
// 로그인 진행이 완료가 되면 시큐리티가 가지고 있는 session을 만들어준다. (key 값 : Security ContextHolder에 세션 정보 저장)
// 시큐리티만 가지고 있는 세션에 들어 갈 수 있는 정보는 => Authentication 타입 객체만 가능
// Authentication 타입 객체 => 안에는 User 정보가 있어야 함
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session > Authentication > UserDetails[PrincipalDetails] 가 들어가게 구성됨

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member; //콤포지션

    private Map<String, Object> attributes;

    private String nameAttributeKey;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public PrincipalDetails(Member member, Map<String, Object> attributes, String nameAttributeKey) {
        this.member = member;
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
    }

    // 해당 유저의 권한을 리턴, spring security 인증 처리
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRoleCode()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // spring security의 userName
    // 인증 할 때 id를 target으로 한다
    @Override
    public String getUsername() {
        return member.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트!! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로 함.
        // LocalDateTime.now() - member.getLoginDate() > 1 year
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
