package com.shoppingmall.config;

import com.shoppingmall.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("패스워드 암호화 가능 여부 테스트")
    void testPasswordEncoding() {
        //given
        String password = "yyaabbdd223434";

        //when
        String encodedPassword = passwordEncoder.encode(password);

        //then
        assertAll(
                () -> assertNotEquals(password, encodedPassword),
                () -> assertTrue(passwordEncoder.matches(password, encodedPassword)) // Hash화 된 pwd == hash 전 pwd 비교
        );
    }

}
