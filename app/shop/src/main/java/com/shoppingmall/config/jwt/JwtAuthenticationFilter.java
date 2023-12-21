package com.shoppingmall.config.jwt;

import com.shoppingmall.config.auth.PrincipalDetails;
import com.shoppingmall.constant.Role;
import com.shoppingmall.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// spring security UsernamePasswordAuthenticationFilter 존재
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작함
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/member/login");
    }

    // login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.debug("JwtAuthenticationFilter : 로그인 시도중");

        try {
            String username = StringUtils.isNotBlank(request.getParameter("username")) ? request.getParameter("username") : "";
            String password = StringUtils.isNotBlank(request.getParameter("password")) ? request.getParameter("password") : "";

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // attemptAuthentication 실행/인증 완료 후 successfulAuthentication 함수 실행
    // JWT 토큰 생성 후 Client에 반환
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("SuccessfulAuthentication exec: finished authentication");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Member loginMember = (principalDetails.getMember() != null) ? principalDetails.getMember() : new Member();

        String username = StringUtils.isNotBlank(loginMember.getAccount()) ? loginMember.getAccount() : "";
        Role role = StringUtils.isNotBlank(loginMember.getRole().getCode()) ? loginMember.getRole() : null;

        String token = jwtTokenProvider.createToken(username, role);
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
