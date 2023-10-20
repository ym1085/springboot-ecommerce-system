package com.shoppingmall.service;

import com.shoppingmall.common.MessageCode;
import com.shoppingmall.constant.Role;
import com.shoppingmall.domain.Member;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.repository.MemberMapper;
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
