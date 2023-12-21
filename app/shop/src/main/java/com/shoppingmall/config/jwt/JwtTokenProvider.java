package com.shoppingmall.config.jwt;

import com.shoppingmall.constant.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Authentication: 인증 토큰(JWT, Bearer Token) 서버로 보낼 때 사용하는 헤더
 * auth: 사용자 권한 값 KEY
 * Bearer: Authorization ( Basic: 사용자 아이디, 암호 Base64 인코딩 값 토큰 사용 | Bearer: JWT, OAuth에 대한 토큰 사용 )
 * TOKEN_TIME: 토큰 만료시간 (60분)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // ms|sec|minutes => 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey; // Base64 encoded Secret Key (application.properties)
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, Role role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String createToken(Authentication authentication) {
        String accessToken = createJwtAccessToken(authentication, getJwtAccessTokenExpireDate());
        String refreshToken = createJwtRefreshToken(authentication, getJwtRefreshTokenExpireDate());

        //todo: TokenDto.builder
        return "";
    }

    private String createJwtAccessToken(Authentication authentication, Date accessTokenExpiration) {
        // 권한들 추출
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORIZATION_KEY, authorities)
                .setExpiration(accessTokenExpiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private String createJwtRefreshToken(Authentication authentication, Date refreshTokenExpiration) {
        return Jwts.builder()
                .setExpiration(refreshTokenExpiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private static Date getJwtAccessTokenExpireDate() {
        long now = (new Date()).getTime();
        return new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    }

    private static Date getJwtRefreshTokenExpireDate() {
        long now = (new Date()).getTime();
        return new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return "";
    }

    // JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰 기반 사용자 정보 추출
    public Claims getUserInfoFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
