package com.shoppingmall.config.auth.jwt;

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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[doFilterInternal] start doFilter");
        String jwtToken = jwtTokenProvider.getJwtFromHeader(request); // header(Authentication)에서 jwt token 추출
        String requestURI = request.getRequestURI();

        if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken); // 토큰이 유효한 경우 토큰에서 Authentication 객체를 가져와 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 = '{}', 인증 정보를 저장하였습니다, uri = '{}'", authentication.getName(), requestURI);
        } else {
            log.info("유효한 JWT 토큰이 존재하지 않습니다. requestURI = '{}'", requestURI);
        }

        filterChain.doFilter(request, response);
    }
}