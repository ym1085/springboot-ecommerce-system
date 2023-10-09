package com.multi.member.service;

import com.multi.common.utils.message.MessageCode;
import com.multi.member.constant.Role;
import com.multi.member.domain.Member;
import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public int signUp(MemberRequestDto memberRequestDto) {
        memberRequestDto.replaceHyphen();
        memberRequestDto.encodeMemberPassword(passwordEncoder);
        memberRequestDto.setMemberRole(Role.USER);

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
