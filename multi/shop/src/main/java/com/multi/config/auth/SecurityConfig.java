package com.multi.config.auth;

import com.multi.member.security.CustomLogInFailerHandler;
import com.multi.member.security.CustomLogInSuccessHandler;
import com.multi.member.security.OAuth2LoginFailureHandler;
import com.multi.member.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogInSuccessHandler customLogInSuccessHandler;
    private final CustomLogInFailerHandler customLogInFailerHandler;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    // Fixme: Get URL from DB when Authorization
    private static final String[] PERMIT_STATIC_URL = {"/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile"};
    private static final String[] PERMIT_MEMBER_URL = {"/member/login", "/member/loginForm", "/member/joinForm", "/member/access-success","/member/access-denied"};
    private static final String[] PERMIT_API_URL = {"/api/v1/**"};
    private static final String[] PERMIT_SWAGGER2_URL = {"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**"};
    private static final String[] PERMIT_SWAGGER3_URL = {"/v3/api-docs/**", "/swagger-ui/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                        .antMatchers(PERMIT_STATIC_URL).permitAll()
                        .antMatchers(PERMIT_MEMBER_URL).permitAll()
                        .antMatchers(PERMIT_API_URL).permitAll()
                        .antMatchers(PERMIT_SWAGGER2_URL).permitAll()
                        .antMatchers(PERMIT_SWAGGER3_URL).permitAll()
                        .anyRequest().authenticated()
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
