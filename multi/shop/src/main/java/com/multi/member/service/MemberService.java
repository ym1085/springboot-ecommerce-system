package com.multi.member.service;

import com.multi.common.utils.message.MessageCode;
import com.multi.member.constant.Role;
import com.multi.member.domain.Member;
import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberMapper.getMemberByAccount(username).orElse(new Member());
        if (StringUtils.isBlank(member.getAccount())) {
            throw new UsernameNotFoundException(username);
        }
        return member;
    }

    @Transactional
    public int signUp(MemberRequestDto memberRequestDto) {
        memberRequestDto.replaceHyphen();
        memberRequestDto.encodeMemberPassword(this.passwordEncoder);
        memberRequestDto.setMemberRole(Role.ROLE_USER);

        Member member = new Member(memberRequestDto);
        if (isExistsDupleMemberAccount(member) > 0) {
            return MessageCode.FAIL.getCode();
        }

        return memberMapper.signUp(member);
    }

    @Transactional(readOnly = true)
    public int checkDuplMemberAccount(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto);
        return isExistsDupleMemberAccount(member);
    }

    private int isExistsDupleMemberAccount(Member member) {
        return memberMapper.checkDuplMemberAccount(member);
    }
}
