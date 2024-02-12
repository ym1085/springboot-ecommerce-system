package com.shoppingmall.service;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.exception.DuplicateMemberAccountException;
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
    public int join(MemberSaveRequestDto memberRequestDto) {
        memberRequestDto.setPassword(encodePassword(memberRequestDto.getPassword()));
        Member member = memberRequestDto.toEntity();

        if (memberMapper.checkDuplicateMemberAccount(member.getAccount()) > 0) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.DUPLICATE_MEMBER_ACCOUNT.getMessage());
            throw new DuplicateMemberAccountException(ErrorCode.DUPLICATE_MEMBER_ACCOUNT);
        }

        int responseCode = memberMapper.join(member);
        if (responseCode == 0) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.FAIL_SAVE_MEMBER.getMessage());
            throw new FailSaveMemberException(ErrorCode.FAIL_SAVE_MEMBER);
        }

        return responseCode;
    }

    public void validateDuplicateMemberAccount(String account) {
        if (memberMapper.checkDuplicateMemberAccount(account) > 0) {
            log.warn("[Occurred Exception] Error Message = {}", ErrorCode.DUPLICATE_MEMBER_ACCOUNT.getMessage());
            throw new DuplicateMemberAccountException(ErrorCode.DUPLICATE_MEMBER_ACCOUNT);
        }
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.NOT_FOUND_MEMBER_PWD.getMessage());
            throw new PasswordNotFoundException(ErrorCode.NOT_FOUND_MEMBER_PWD);
        }
        return passwordEncoder.encode(password);
    }
}
