package com.shoppingmall.config.auth;

import com.shoppingmall.handler.CustomLogInFailerHandler;
import com.shoppingmall.handler.CustomLogInSuccessHandler;
import com.shoppingmall.handler.PrincipalOAuth2LoginFailureHandler;
import com.shoppingmall.handler.PrincipalOAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 구글 로그인 완료 후 후처리 필요
// 1. 코드받기(인증)
// 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
// 4-2. 쇼핑몰 아닌 경우 (이메일, 전화번호, 이름, 아이디)
// 4-3. 쇼핑몰 같이 부가 정보 같이 저장 해야하는 경우 회원가입 2단계 진행 -> (집주소), 백화점몰 -> (VIP등급, 일반등급)

@Configuration
@EnableWebSecurity // spring security filter -> spring filter chain 에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 어노테이션 활성화, @PreAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final PrincipalOAuth2LoginSuccessHandler principalOAuth2LoginSuccessHandler;
    private final PrincipalOAuth2LoginFailureHandler principalOAuth2LoginFailureHandler;

    private final PrincipalUserDetailsService principalUserDetailsService;
    private final CustomLogInSuccessHandler customLogInSuccessHandler;
    private final CustomLogInFailerHandler customLogInFailerHandler;

    // TODO: DB로 빼서 관리 해야함 URL 같은 경우, 우선 배열로 관리
    private static final String[] PUBLIC_MATCHERS = {
            "/css/**",
            "/img/**",
            "/js/**",
            "/favicon.ico",
            "/h2-console/**",
            "/member/loginForm",
            "/member/joinForm",
            "/api/v1/member/join",
            "/api/v1/member/exists",
            "/api/v1/email/verify",
            "/api/v1/email/verify-request",
            "/api/v1/cart/**"
    };

    private static final String[] USER_MATCHERS = {
            "/api/v1/**",
            "/post/**",
            "/product/**",
            "/cart/**",
            "/"
    };

    private static final String[] ADMIN_MATCHERS = {
            "/admin/**"
    };

    private static final String[] MANAGER_MATCHERS = {
            "/manager/**"
    };

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic().disable() // 서비스 오픈시 비활성화
                .authorizeRequests(authorize -> authorize
                        .antMatchers(PUBLIC_MATCHERS).permitAll()
                        .antMatchers(USER_MATCHERS).hasAnyRole("ADMIN", "MANAGER", "USER")
                        .antMatchers(ADMIN_MATCHERS).hasRole("ADMIN")
                        .antMatchers(MANAGER_MATCHERS).hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/member/loginForm")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/member/login")
                        .successHandler(customLogInSuccessHandler)
                        .failureHandler(customLogInFailerHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/member/loginForm")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                )
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(3600)
                        .userDetailsService(principalUserDetailsService)
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/member/loginForm")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/member/loginForm")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/404")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/member/loginForm")
                        .successHandler(principalOAuth2LoginSuccessHandler)
                        .failureHandler(principalOAuth2LoginFailureHandler)
                        .userInfoEndpoint()
                        .userService(principalOAuth2UserService)
                );

        return http.build();
    }
}
