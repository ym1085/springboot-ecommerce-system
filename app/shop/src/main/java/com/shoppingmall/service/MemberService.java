package com.shoppingmall.service;

import com.shoppingmall.constant.Role;
import com.shoppingmall.domain.Member;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.DuplMemberAccountException;
import com.shoppingmall.exception.FailSaveMemberException;
import com.shoppingmall.exception.PasswordNotFoundException;
import com.shoppingmall.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public int join(MemberRequestDto memberRequestDto) {
        memberRequestDto.setPhoneNumber(replacePhoneNumberHyphen(memberRequestDto.getPhoneNumber()));
        memberRequestDto.setBirthDate(replaceBirthDateHyphen(memberRequestDto.getBirthDate()));
        memberRequestDto.setPassword(encryptMemberPassword(memberRequestDto.getPassword()));
        memberRequestDto.setRole(Role.USER);

        Member member = new Member(memberRequestDto);
        if (isExistsDuplMemberAccount(member)) {
            throw new DuplMemberAccountException();
        }

        int responseCode = memberMapper.join(member);
        if (responseCode == 0) {
            throw new FailSaveMemberException();
        }

        return memberMapper.join(member);
    }

    @Transactional(readOnly = true)
    public int checkDuplMemberAccount(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto);
        int responseCode = 0;
        if (isExistsDuplMemberAccount(member)) {
            responseCode = 1;
        }
        return responseCode;
    }

    private boolean isExistsDuplMemberAccount(Member member) {
        return memberMapper.checkDuplMemberAccount(member) > 0;
    }

    private String replacePhoneNumberHyphen(String phoneNumber) {
        return (StringUtils.isNotBlank(phoneNumber)) ? phoneNumber.replace("-", "") : phoneNumber;
    }

    private String replaceBirthDateHyphen(String birthDate) {
        return (StringUtils.isNotBlank(birthDate)) ? birthDate.replace("-", "") : birthDate;
    }

    private String encryptMemberPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new PasswordNotFoundException();
        }
        return passwordEncoder.encode(password);
    }
}
