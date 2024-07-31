package com.shoppingmall.service;

import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.exception.MemberException;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.shoppingmall.common.code.failure.member.MemberFailureCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final RedisUtils redisUtils;

    @Transactional
    public void join(MemberSaveRequestDto memberRequestDto) {
        memberRequestDto.setPassword(encodePassword(memberRequestDto.getPassword()));

        if (memberMapper.checkDuplicateMemberAccount(memberRequestDto.getAccount()) > 0) {
            log.error(DUPLICATE_MEMBER_ACCOUNT.getMessage());
            throw new MemberException(DUPLICATE_MEMBER_ACCOUNT);
        }

        if (memberMapper.join(memberRequestDto) < 1) {
            throw new MemberException(SAVE_MEMBER);
        }
    }

    public void validateDuplicateMemberAccount(String account) {
        if (memberMapper.checkDuplicateMemberAccount(account) > 0) {
            log.warn(DUPLICATE_MEMBER_ACCOUNT.getMessage());
            throw new MemberException(DUPLICATE_MEMBER_ACCOUNT);
        }
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            log.error(NOT_FOUND_MEMBER_PWD.getMessage());
            throw new MemberException(NOT_FOUND_MEMBER_PWD);
        }
        return passwordEncoder.encode(password);
    }
}
