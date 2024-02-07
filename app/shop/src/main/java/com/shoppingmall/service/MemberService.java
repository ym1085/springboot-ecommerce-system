package com.shoppingmall.service;

import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.exception.DuplicateMemberAccountException;
import com.shoppingmall.exception.FailSaveMemberException;
import com.shoppingmall.exception.PasswordNotFoundException;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.utils.RedisUtils;
import com.shoppingmall.vo.MemberVO;
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
    public int join(MemberSaveRequestDto memberRequestDto) {
        memberRequestDto.setPassword(encodePassword(memberRequestDto.getPassword()));
        MemberVO member = memberRequestDto.toEntity();

        if (memberMapper.checkDuplicateMemberAccount(member.getAccount()) > 0) {
            throw new DuplicateMemberAccountException();
        }

        int responseCode = memberMapper.join(member);
        if (responseCode == 0) {
            throw new FailSaveMemberException();
        }

        return responseCode;
    }

    public void validateDuplicateMemberAccount(String account) {
        if (memberMapper.checkDuplicateMemberAccount(account) > 0) {
            throw new DuplicateMemberAccountException();
        }
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new PasswordNotFoundException();
        }
        return passwordEncoder.encode(password);
    }
}
