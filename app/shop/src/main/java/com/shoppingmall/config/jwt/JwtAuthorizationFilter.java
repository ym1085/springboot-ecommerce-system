package com.shoppingmall.config.jwt;

import com.shoppingmall.config.auth.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestJwtToken = jwtTokenProvider.getJwtFromHeader(request);
        if (!StringUtils.hasText(requestJwtToken)) {
            return;
        }

        if (!jwtTokenProvider.validateToken(requestJwtToken)) {
            log.error("Invalid JWT token");
            return;
        }

        Claims userInfoFromToken = jwtTokenProvider.getUserInfoFromToken(requestJwtToken);

        try {
            setAuthentication(userInfoFromToken.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
    }

    // 인증 처리
    private void setAuthentication(String username) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // 인증용 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}