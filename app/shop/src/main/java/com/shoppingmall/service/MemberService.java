package com.shoppingmall.service;

import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.DuplMemberAccountException;
import com.shoppingmall.exception.FailSaveMemberException;
import com.shoppingmall.exception.PasswordNotFoundException;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.utils.RedisUtils;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final RedisUtils redisUtils;

    @Transactional
    public int join(MemberRequestDto memberRequestDto) {
        memberRequestDto.setPassword(encodePassword(memberRequestDto.getPassword()));
        Member member = memberRequestDto.toEntity();

        if (memberMapper.checkDuplMemberAccount(member.getAccount()) > 0) {
            throw new DuplMemberAccountException();
        }

        int responseCode = memberMapper.join(member);
        if (responseCode == 0) {
            throw new FailSaveMemberException();
        }

        return responseCode;
    }

    public int validateDuplicateMemberAccount(String account) {
        return memberMapper.checkDuplMemberAccount(account) > 0 ? 0 : 1;
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new PasswordNotFoundException();
        }
        return passwordEncoder.encode(password);
    }
}
