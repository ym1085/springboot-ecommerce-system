package com.shoppingmall.service;

import com.shoppingmall.constant.Role;
import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.exception.MemberException;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.utils.RedisUtils;
import com.shoppingmall.vo.Member;
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
        memberRequestDto.setRole(Role.ADMIN);
        memberRequestDto.setPassword(encodePassword(memberRequestDto.getPassword()));

        Member member = memberRequestDto.toEntity();
        if (memberMapper.checkDuplicateMemberAccount(member.getAccount()) > 0) {
            log.error(DUPLICATE_MEMBER_ACCOUNT.getMessage());
            throw new MemberException(DUPLICATE_MEMBER_ACCOUNT);
        }

        if (memberMapper.join(member) < 1) {
            throw new MemberException(SAVE_MEMBER);
        }
    }

    public void validateDuplicateMemberAccount(String account) {
        if (memberMapper.checkDuplicateMemberAccount(account) > 0) {
            log.warn("[Occurred Exception] Error Message = {}", DUPLICATE_MEMBER_ACCOUNT.getMessage());
            throw new MemberException(DUPLICATE_MEMBER_ACCOUNT);
        }
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            log.error("[Occurred Exception] Error Message = {}", NOT_FOUND_MEMBER_PWD.getMessage());
            throw new MemberException(NOT_FOUND_MEMBER_PWD);
        }
        return passwordEncoder.encode(password);
    }
}
