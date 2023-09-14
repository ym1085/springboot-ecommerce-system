package com.multi.member.service.impl;

import com.multi.common.utils.message.MessageCode;
import com.multi.member.constant.Role;
import com.multi.member.domain.Member;
import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.repository.MemberMapper;
import com.multi.member.service.MemberService;
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
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public int signUp(MemberRequestDto memberRequestDto) {
        memberRequestDto.replaceHyphen();
        memberRequestDto.encodeMemberPassword(this.passwordEncoder);
        memberRequestDto.setMemberRole(Role.ROLE_USER);

        Member member = new Member(memberRequestDto);
        if (isExistsDupleMemberAccount(member) > 0) {
            //TODO: 예외 던지지 말고, response code 던져주는걸로 수정
            throw new IllegalArgumentException("중복된 회원입니다. 다시 시도해주세요.");
        }

        return memberMapper.signUp(member);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Information about the user trying to sign in, username = {}", username);
        Member member = memberMapper.getMemberByAccount(username).orElse(new Member());
        if (StringUtils.isBlank(member.getAccount())) {
            throw new UsernameNotFoundException(username);
        }
        return member;
    }

    @Transactional(readOnly = true)
    @Override
    public int checkDuplMemberAccount(MemberRequestDto memberRequestDto) {
        if (StringUtils.isBlank(memberRequestDto.getAccount())) {
            log.error("member account is not exists");
            return MessageCode.FAIL.getCode();
        }

        Member member = new Member(memberRequestDto); // dto -> entity
        return isExistsDupleMemberAccount(member);
    }

    /**
     * 중복 회원 검증
     */
    private int isExistsDupleMemberAccount(Member member) {
        return memberMapper.checkDuplMemberAccount(member);
    }
}
