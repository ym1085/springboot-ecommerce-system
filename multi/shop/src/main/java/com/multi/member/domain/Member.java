package com.multi.member.domain;

import com.multi.member.constant.Gender;
import com.multi.member.constant.Role;
import com.multi.member.dto.request.MemberRequestDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Member implements UserDetails {
    private Long memberId;
    private String name;
    private String account;
    private String password;
    private String email;
    private String phoneNumber;
    private String picture;
    private String birthDate;
    private String certYn;
    private Role role;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Gender gender;
    private String registrationId;
    private String providerToken;

    // Dto -> Entity(vo)
    public Member(MemberRequestDto memberRequestDto) {
        this.name = memberRequestDto.getName();
        this.account = memberRequestDto.getAccount();
        this.password = memberRequestDto.getPassword();
        this.email = memberRequestDto.getEmail();
        this.phoneNumber = memberRequestDto.getPhoneNumber();
        this.picture = memberRequestDto.getPicture();
        this.birthDate = memberRequestDto.getBirthDate();
        this.certYn = memberRequestDto.getCertYn();
        this.gender = memberRequestDto.getGender();
    }

    @Builder
    public Member(String name, String account, String email, String picture, Role role, String registrationId, String providerToken) {
        this.name = name;
        this.account = account;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.registrationId = registrationId;
        this.providerToken = providerToken;
    }

    // 클라이언트가 소셜 로그인를 시도하는 사용자가 제공되는 정보(data)에 변경이 있으면
    // 해당 데이터를 받아서 DB에 실제 업데이트 하기 전 값 셋팅을 위해 사용
    public Member updateRenewalMember(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    // spring security 인증 처리를 위해 UserDetails의 메서드 오버라이딩
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    // spring security의 userName
    // 인증 할 때 id를 target으로 한다
    @Override
    public String getUsername() {
        return this.account;
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
        return true;
    }
}
