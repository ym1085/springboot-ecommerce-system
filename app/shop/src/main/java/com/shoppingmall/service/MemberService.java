package com.shoppingmall.service;

import com.shoppingmall.domain.Member;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.DuplMemberAccountException;
import com.shoppingmall.exception.FailSaveMemberException;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.utils.MemberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final MemberUtils memberUtils;

    @Transactional
    public int join(MemberRequestDto memberRequestDto) {
        MemberRequestDto memberInfo = memberUtils.getMemberInfo(memberRequestDto);
        Member member = new Member(memberInfo);
        if (isExistsDuplMemberAccount(member)) {
            throw new DuplMemberAccountException();
        }

        int responseCode = memberMapper.join(member);
        if (responseCode == 0) {
            throw new FailSaveMemberException();
        }

        return responseCode;
    }

    @Transactional(readOnly = true)
    public int checkDuplMemberAccount(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto);
        int responseCode = 1;
        if (isExistsDuplMemberAccount(member)) {
            responseCode = 0;
        }
        return responseCode;
    }

    private boolean isExistsDuplMemberAccount(Member member) {
        return memberMapper.checkDuplMemberAccount(member) > 0;
    }
}
