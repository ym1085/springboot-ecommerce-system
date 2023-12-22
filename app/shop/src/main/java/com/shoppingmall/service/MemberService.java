package com.shoppingmall.service;

import com.shoppingmall.config.jwt.JwtTokenProvider;
import com.shoppingmall.config.jwt.dto.request.JwtToken;
import com.shoppingmall.domain.Member;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.DuplMemberAccountException;
import com.shoppingmall.exception.FailSaveMemberException;
import com.shoppingmall.exception.PasswordNotFoundException;
import com.shoppingmall.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

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

    public JwtToken login(String username, String password) {
        // username, passowrd 기반 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // authenticate 함수 통해 Member 에 대한 검증 진행
        // 위 함수 실행될 때 실제 loadUserByUsername 함수가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보 기반 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticate);
        return jwtToken;
    }

    public int checkDuplMemberAccount(String account) {
        int responseCode = 1;
        if (memberMapper.checkDuplMemberAccount(account) > 0) {
            responseCode = 0;
        }
        return responseCode;
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new PasswordNotFoundException();
        }
        return passwordEncoder.encode(password);
    }
}
