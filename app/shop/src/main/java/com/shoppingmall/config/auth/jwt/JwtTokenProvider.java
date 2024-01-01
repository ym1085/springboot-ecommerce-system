package com.shoppingmall.config.auth.jwt;

import com.shoppingmall.dto.request.JwtTokenDto;
import com.shoppingmall.utils.RedisUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
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
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // ms|sec|minutes => access token 유효 시간은 => 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // refresh token 유효 시간은 => 7일

    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private static final String CLAIM_TYPE_NAME = "type";
    private final RedisUtils redisUtils;

    @Value("${jwt.secret.key}")
    private String secretKey; // Base64 encoded Secret Key (application.properties)
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public JwtTokenDto generateToken(Authentication authentication) {
        return this.generateToken(authentication.getName(), authentication.getAuthorities());
    }

    // Access + Refresh Token 생성 후 JwtToken 반환
    public JwtTokenDto generateToken(String name, Collection<? extends GrantedAuthority> inputAuthorities) {
        String authorities = extractedMemberAuthorites(inputAuthorities);

        String accessToken = createJwtAccessToken(name, authorities, getJwtAccessTokenExpireDate());
        String refreshToken = createJwtRefreshToken(name, authorities, getJwtRefreshTokenExpireDate());

        return JwtTokenDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    private String extractedMemberAuthorites(Collection<? extends GrantedAuthority> inputAuthorities) {
        return inputAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // JWT Access Token 생성
    private String createJwtAccessToken(String name, String authorities, Date accessTokenExpiration) {
        return Jwts.builder()
                .setSubject(name)
                .claim(AUTHORIZATION_KEY, authorities)
                .claim(CLAIM_TYPE_NAME, TYPE_ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT Refresh Token 생성
    private String createJwtRefreshToken(String name, String authorities, Date refreshTokenExpiration) {
        return Jwts.builder()
                .claim(CLAIM_TYPE_NAME, TYPE_REFRESH)
                .setIssuedAt(new Date())
                .setExpiration(refreshTokenExpiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT Access Token 만료 기간 산정
    private static Date getJwtAccessTokenExpireDate() {
        long now = (new Date()).getTime();
        return new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    }

    // JWT Refresh Token 만료 기간 산정
    private static Date getJwtRefreshTokenExpireDate() {
        long now = (new Date()).getTime();
        return new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
    }

    // JWT HttpServletRequest 의 Header 에서 Authorization 필드(Key) 를 읽어서 token 정보를 획득
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getUserInfoFromToken(accessToken); // JWT 토큰 복호화

        if (claims.get(AUTHORIZATION_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 유저 권한 정보를 가져온다
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("validateToken 검증 = {}", token);
            if (redisUtils.hasKeyBlackList(token)) {
                log.error("Redis 블랙 리스트에 존재하는 token으로 해당 토큰은 재확인이 필요합니다, token = {}", token);
                return false; // 사용자가 로그아웃을 했거나, 탈취된 refresh token으로 봐야함
            }
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 형식의 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다(PARAMETER)", e);
        } catch (Exception e) {
            log.error("JWT 토큰을 파싱 하는 과정 중 서버 오류가 발생하였습니다!", e);
        }
        return false;
    }

    // 토큰 기반 사용자 정보 추출
    public Claims getUserInfoFromToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // JWT 토큰(accessToken) 남은 유효시간을 얻어오는 함수
    public Long getExpirationFromToken(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();

        // accessToken 남은 유효시간 - 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
