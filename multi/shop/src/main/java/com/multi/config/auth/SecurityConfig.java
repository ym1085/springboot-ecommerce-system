package com.multi.config.auth;

import com.multi.member.constant.Role;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // detect csrf attack
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                        .antMatchers("/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                        //.antMatchers("/", "/member/login", "/member/loginForm", "/member/joinForm").permitAll() // Todo: main 페이지 만들고 변경
                        .antMatchers("/member/login", "/member/loginForm", "/member/joinForm").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/member/loginForm") // 사용자 정의 로그인 페이지
                    .loginProcessingUrl("/member/login/success") // 로그인 성공 후 이동 페이지
                    .usernameParameter("account") // 아이디 파라미터명
                    .passwordParameter("password") // 패스워드 파라미터명
                    .loginProcessingUrl("/member/login") // 로그인 for action url
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
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
