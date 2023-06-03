package com.multi.config.auth;

import com.multi.member.constant.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @EnableWebSecurity
 * WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 선언 시 SpringSecurityFilterChain이 자동으로 포함됨
 * WebSecurityConfigurerAdapter를 상속 후 메서드 오버라이딩을 통해 보안 설정 커스텀 가능
 */
@Configuration // Spring에서 설정 파일 등록 시 사용 -> @Bean 어노테이션이랑 같이 엮어서 사용 -> Component scan target
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-consonle/**", "/profile").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.ROLE_USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login().userInfoEndpoint()
                    .userService(null)
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
