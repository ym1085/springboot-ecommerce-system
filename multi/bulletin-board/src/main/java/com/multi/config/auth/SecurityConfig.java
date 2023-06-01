package com.multi.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @EnableWebSecurity
 * WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 선언 시 SpringSecurityFilterChain이 자동으로 포함됨
 * WebSecurityConfigurerAdapter를 상속 후 메서드 오버라이딩을 통해 보안 설정 커스텀 가능
 */
@Configuration // Spring에서 설정 파일 등록 시 사용 -> @Bean 어노테이션이랑 같이 엮어서 사용 -> Component scan target
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); // /static 하위 디렉토리는 인증 무시
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable() // Todo: Security 상세 내용 수정
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable();
//                .authorizeRequests()
//                .antMatchers("/", "/member");
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
