package com.multi.member.service.impl;

import com.multi.member.domain.Member;
import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.dto.response.MemberResponseDto;
import com.multi.member.repository.MemberMapper;
import com.multi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public MemberResponseDto signUp(MemberRequestDto memberRequestDto) {
        memberRequestDto.replaceHyphen(); // 휴대폰, 생년월일 '-' 제거
        memberRequestDto.encodeMemberPassword(this.passwordEncoder); // encrypt member password

        Member member = new Member(memberRequestDto);
        Long memberId = memberMapper.signUp(member);

        return memberMapper.getMemberById(memberId)
                .filter(Objects::nonNull)
                .map(entity -> new MemberResponseDto(entity))
                .orElse(new MemberResponseDto());
    }
}
