package com.shoppingmall.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// security가 filter를 가지고 있는데 해당 필터 중 BasicAuthenticationFilter 라는 것이 존재
// 권한이나 인증이 필요한 특정 URL 요청 시 위 필터를 무조건 타게 되어있음
// 만약 권한/인증이 필요한 URL이 아니면 아래 필터를 타지 않음
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = jwtTokenProvider.getJwtFromHeader(request); // Header에서 JWT Token 추출

        if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
            // 토큰이 유효한 경우 토큰에서 Authentication 객체를 가져와 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}