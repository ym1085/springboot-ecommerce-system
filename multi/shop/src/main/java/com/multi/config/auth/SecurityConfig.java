package com.multi.config.auth;

import com.multi.member.constant.Role;
import com.multi.member.security.CustomLogInFailerHandler;
import com.multi.member.security.CustomLogInSuccessHandler;
import com.multi.member.security.OAuth2LoginFailureHandler;
import com.multi.member.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogInSuccessHandler customLogInSuccessHandler;
    private final CustomLogInFailerHandler customLogInFailerHandler;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                        .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                        .antMatchers("/").hasAnyRole(Role.ADMIN.name(), Role.USER.name(), Role.GUEST.name())
                        .anyRequest().permitAll()
                .and()
                    .formLogin()
                        .loginPage("/member/loginForm")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/member/login")
                        .successHandler(customLogInSuccessHandler)
                        .failureHandler(customLogInFailerHandler)
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 로그아웃 이후에 모든 세션 삭제
                        .deleteCookies("JSESSIONID", "remember-me")
                        //.addLogoutHandler(logoutHandler())
                        //.logoutSuccessHandler(logoutSuccessHandler())
                .and()
                    .sessionManagement()  // Todo: Add other session managements options
                        .invalidSessionUrl("/member/loginForm")
                .and()
                    .oauth2Login()
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService);

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
