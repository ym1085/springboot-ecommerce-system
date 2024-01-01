package com.shoppingmall.service;

import com.shoppingmall.common.SuccessCode;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

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

    @Transactional
    public JwtTokenDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        MemberResponseDto memberResponseDto = verifyMatchedMemberPassword(loginRequestDto);
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authenticate);

        redisUtils.setValues(authenticate.getName(), jwtTokenDto.getRefreshToken(), jwtTokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS); // key: account, value: refresh token
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

    @Transactional
    public int logout(RefreshTokenDto refreshTokenDto) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshTokenDto.getAccessToken())) {
            throw new IllegalArgumentException("로그아웃 시도 중, 유효하지 않은 JWT Token 값 입니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenDto.getAccessToken());

        // Redis에서 user account 기반 저장된 refresh token을 가져와서 유효성 검사 후, refresh token 존재하면 삭제 진행
        String redisRefreshToken = redisUtils.getValues(authentication.getName());
        if (StringUtils.hasText(redisRefreshToken)) {
            boolean isDeleteRedisValues = redisUtils.deleteValues(authentication.getName()); // delete refresh token from redis
            if (isDeleteRedisValues) {
                log.info("Redis에서 user account key 삭제에 성공 하였습니다, user account = {}", authentication.getName());
            }
        }

        // 해당 access token 유효시간 가지고 와서 black list로 저장
        Long refreshTokenExpirationTime = jwtTokenProvider.getExpirationFromToken(refreshTokenDto.getAccessToken());

        // black list 지정, 기존에는 setValues(username, .., ..) 형식으로 저장됨 여기서는 access token을 넣는다
        redisUtils.setBlackList(refreshTokenDto.getAccessToken(), "logout", refreshTokenExpirationTime, TimeUnit.MILLISECONDS);

        return SuccessCode.SUCCESS.getCode();
    }

    public JwtTokenDto reissue(RefreshTokenDto refreshTokenDto) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshTokenDto.getAccessToken())) {
            throw new IllegalArgumentException("Token 재발급을 받는 과정 중, 유효하지 않은 refresh token이 요청 되었습니다");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenDto.getAccessToken());

        // Redis에서 user account 기반 refresh token 을 획득
        String refreshToken = redisUtils.getValues(authentication.getName());
        if (ObjectUtils.isEmpty(refreshToken)) { // 로그아웃 되어서 refresh token이 존재하지 않는 경우 처리
            throw new IllegalArgumentException("토큰이 존재하지 않습니다(로그아웃, 회원탈퇴)");
        }

        // refresh token 일치 여부 확인, 일치 하지 않을 경우 Exception 발생
        redisUtils.checkRefreshToken(authentication.getName(), refreshTokenDto.getRefreshToken()); // key: account, value: refresh token

        // 신규 토큰 생성
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication);

        // redis에 refresh token 업데이트
        redisUtils.setValues(authentication.getName(), jwtTokenDto.getRefreshToken(), jwtTokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

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
