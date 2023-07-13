package com.multi.config.auth;

import com.multi.member.constant.Role;
import com.multi.member.security.CustomLogInSuccessHandler;
import com.multi.member.security.OAuth2LoginFailureHandler;
import com.multi.member.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// https://samori.tistory.com/64
// https://lealea.tistory.com/211
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogInSuccessHandler customLogInSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // detect csrf(cross site request forgery) attack disable
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                        .antMatchers("/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                        .antMatchers("/member/login", "/member/loginForm", "/member/joinForm", "/member/access-denied").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                .and()
                    .formLogin() // formLogin 인증 방식을 사용한다는 의미
                    .loginPage("/member/loginForm") // 사용자 정의 로그인 페이지
                    //.defaultSuccessUrl("/member/login/success") // 로그인 성공 후 이동 페이지
                    .usernameParameter("username") // 아이디 파라미터명
                    .passwordParameter("password") // 패스워드 파라미터명
                    .loginProcessingUrl("/member/login") // 로그인 action url
                    .failureUrl("/member/access-denied")
                    .defaultSuccessUrl("/member/access-success") // 로그인 성공 후 이동 페이지
                    //.successHandler(customLogInSuccessHandler) // 로그인 성공 후 일반 로그인 Handling
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                    .successHandler(oAuth2LoginSuccessHandler) // 로그인 성공 후 OAuth 2.0 로그인 Handling
                    .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패 시 실패 Handling
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);
    }

    /**
     * 비밀번호를 DB에 그대로 저장하는 경우, DB가 해킹 당하면 보안 이슈 발생
     * BCryptPasswordEncoder 해시 함수를 이용 하여 비밀번호 암호화 후 저장
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
