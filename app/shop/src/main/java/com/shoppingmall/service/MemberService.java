package com.shoppingmall.service;

import com.shoppingmall.config.auth.jwt.JwtTokenProvider;
import com.shoppingmall.dto.request.JwtTokenDto;
import com.shoppingmall.dto.request.LoginRequestDto;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.dto.request.RefreshTokenDto;
import com.shoppingmall.dto.response.MemberResponseDto;
import com.shoppingmall.exception.DuplMemberAccountException;
import com.shoppingmall.exception.FailSaveMemberException;
import com.shoppingmall.exception.PasswordNotFoundException;
import com.shoppingmall.exception.PasswordNotMatchedException;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.mapper.RefreshTokenMapper;
import com.shoppingmall.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenMapper refreshTokenMapper;
    private final RedisService redisService;

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

    @Transactional
    public JwtTokenDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        MemberResponseDto memberResponseDto = verifyMatchedMemberPassword(loginRequestDto);
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authenticate);

        redisService.setValue(authenticate.getName(), jwtTokenDto.getRefreshToken());
        return jwtTokenDto;
    }

    public MemberResponseDto verifyMatchedMemberPassword(LoginRequestDto loginRequestDto) {
        MemberResponseDto memberResponseDto = memberMapper.getMemberByAccount(loginRequestDto.getUsername())
                .map(MemberResponseDto::toDto)
                .orElse(new MemberResponseDto());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), memberResponseDto.getPassword())) {
            throw new PasswordNotMatchedException();
        }
        return memberResponseDto;
    }

    public JwtTokenDto reissue(RefreshTokenDto refreshTokenDto) {
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenDto.getAccessToken());
        redisService.checkRefreshToken(authentication.getName(), refreshTokenDto.getRefreshToken()); // key: account, value: refresh token

        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication);
        return jwtTokenDto;
    }

    public int checkDuplMemberAccount(String account) {
        return memberMapper.checkDuplMemberAccount(account) > 0 ? 0 : 1;
    }

    public MemberResponseDto getMemberById(Long memberId) {
        return memberMapper.getMemberById(memberId)
                .map(MemberResponseDto::toDto)
                .orElse(new MemberResponseDto());
    }

    private String encodePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new PasswordNotFoundException();
        }
        return passwordEncoder.encode(password);
    }
}
