package com.shoppingmall.service;

import com.shoppingmall.common.MessageCode;
import com.shoppingmall.constant.Role;
import com.shoppingmall.domain.Member;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.PasswordNotFoundException;
import com.shoppingmall.repository.MemberMapper;
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
    public MessageCode join(MemberRequestDto memberRequestDto) {
        memberRequestDto.setPhoneNumber(replacePhoneNumberHyphen(memberRequestDto.getPhoneNumber()));
        memberRequestDto.setBirthDate(replaceBirthDateHyphen(memberRequestDto.getBirthDate()));
        memberRequestDto.setPassword(encryptMemberPassword(memberRequestDto.getPassword()));
        memberRequestDto.setRole(Role.USER);

        Member member = new Member(memberRequestDto);
        if (isExistsDuplMemberAccount(member) > 0) {
            return MessageCode.FAIL_DUPL_MEMBER;
        }

        return (memberMapper.join(member) > 0)
                ? MessageCode.SUCCESS_SAVE_MEMBER
                : MessageCode.FAIL_SAVE_MEMBER;
    }

    @Transactional(readOnly = true)
    public MessageCode checkDuplMemberAccount(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto);
        return (isExistsDuplMemberAccount(member) > 0)
                ? MessageCode.FAIL_DUPL_MEMBER
                : MessageCode.SUCCESS_DUPL_ACCOUNT;
    }

    private int isExistsDuplMemberAccount(Member member) {
        return memberMapper.checkDuplMemberAccount(member);
    }

    private String replacePhoneNumberHyphen(String phoneNumber) {
        return (StringUtils.isNotBlank(phoneNumber)) ? phoneNumber.replace("-", "") : phoneNumber;
    }

    private String replaceBirthDateHyphen(String birthDate) {
        return (StringUtils.isNotBlank(birthDate)) ? birthDate.replace("-", "") : birthDate;
    }

    private String encryptMemberPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new PasswordNotFoundException(MessageCode.NOT_FOUND_MEMBER_PASSWORD);
        }
        return passwordEncoder.encode(password);
    }
}
