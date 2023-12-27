package com.shoppingmall.config.auth;

import com.shoppingmall.config.jwt.JwtAccessDeniedHandler;
import com.shoppingmall.config.jwt.JwtAuthenticationEntryPoint;
import com.shoppingmall.config.jwt.JwtSecurityConfig;
import com.shoppingmall.config.jwt.JwtTokenProvider;
import com.shoppingmall.handler.PrincipalOAuth2LoginFailureHandler;
import com.shoppingmall.handler.PrincipalOAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

// 구글 로그인 완료 후 후처리 필요
// 1. 코드받기(인증), 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보를 가져오고 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
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

    private final CorsFilter corsFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .headers().frameOptions().disable()
                .and()
                    .addFilter(corsFilter)
                    .authorizeRequests()
                        .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                        .antMatchers("/member/loginForm").permitAll()
                        .antMatchers("/member/joinForm").permitAll()
                        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                        .antMatchers("/post/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGENT') or hasRole('ROLE_USER')")
                        .antMatchers("/payment/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGENT') or hasRole('ROLE_USER')")
                        .anyRequest().permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT는 HttpSession 사용 안함
                .and()
                    .apply(new JwtSecurityConfig(jwtTokenProvider))
                .and()
                    .oauth2Login()
                        .loginPage("/member/loginForm")
                        .successHandler(principalOAuth2LoginSuccessHandler)
                        .failureHandler(principalOAuth2LoginFailureHandler)
                        .userInfoEndpoint()
                        .userService(principalOAuth2UserService);

        return http.build();
    }
}
