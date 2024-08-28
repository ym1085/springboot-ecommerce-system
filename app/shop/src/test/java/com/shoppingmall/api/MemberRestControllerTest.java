package com.shoppingmall.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.ShopApplication;
import com.shoppingmall.constant.Gender;
import com.shoppingmall.constant.Role;
import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MemberRestController.class)
@ContextConfiguration(classes = { ShopApplication.class })
@WithMockUser(username = "admin", roles = {"USER"})
class MemberRestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 성공")
    @Order(1)
    void testSignUp() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName("김영민")
                .account("test01")
                .password("yyyyjjj*1")
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(200)))
                .andExpect(jsonPath("$.message", Matchers.containsString("회원가입에 성공")));
    }

    @Test
    @DisplayName("이름이 null인 경우 테스트")
    @Order(2)
    void testMemberNameNull() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName(null)
                .account("test01")
                .password("yyyyjjj*1")
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("이름은 필수 입력 항목 입니다")));
    }

    @Test
    @DisplayName("이름이 한글자 미만인 경우 테스트")
    @Order(3)
    void testMemberNameLessThan1() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName("김")
                .account("test01")
                .password("yyyyjjj*1")
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요")));
    }

    @Test
    @DisplayName("이름이 여섯글자 초과인 경우 테스트")
    @Order(4)
    void testMemberNameOverThan6() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName("산다라박이무니다")
                .account("test01")
                .password("yyyyjjj*1")
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요")));
    }

    @Test
    @DisplayName("ID가 null인 경우 테스트")
    @Order(5)
    void testMemberIdNull() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName("산다라박이무니다")
                .account(null)
                .password("yyyyjjj*1")
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("ID는 필수 입력 항목 입니다")));
    }

    @Test
    @DisplayName("ID가 30자 이상인 경우 테스트 - 예외 발생")
    @Order(6)
    void testMemberIdOverThan30() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName("산다라박이무니다")
                .account("aaaaaaaaaabbbbbbbbbbdddddddddd3")
                .password("yyyyjjj*1")
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("30자 이하의 ID만 입력 가능합니다")));
    }

    @Test
    @DisplayName("비밀번호가 null인 경우")
    @Order(7)
    void testMemberPasswordNull() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .userName("산다라박이무니다")
                .account("aaaaaaaaaabbbbbbbbbbdddddddddd3")
                .password(null)
                .email("test@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("비밀번호는 필수 입력 항목 입니다")));
    }

    @ParameterizedTest
    @MethodSource("validationMemberPasswordGroup")
    @DisplayName("비밀번호 형식에 맞지 않는 경우")
    @Order(8)
    void testMemberPasswordNotMatchedPattern(MemberSaveRequestDto memberRequestDto) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요")));
    }

    private static Stream<Arguments> validationMemberPasswordGroup() {
        MemberSaveRequestDto memberRequestDto1 = MemberSaveRequestDto.builder()
                .userName("김주영")
                .account("user01")
                .password("sjsjsjsjs")
                .email("user01@gmail.com")
                .phoneNumber("010-1111-4444")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1993-08-23")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto2 = MemberSaveRequestDto.builder()
                .userName("김하늘")
                .account("user02")
                .password("23213213213")
                .email("hadmdm@gmail.com")
                .phoneNumber("010-1111-4444")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1950-04-23")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto3 = MemberSaveRequestDto.builder()
                .userName("노홍철")
                .account("user03")
                .password("*&^%$#@!")
                .email("hong@gmail.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1988-08-29")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto4 = MemberSaveRequestDto.builder()
                .userName("강호동")
                .account("user04")
                .password("1234567")
                .email("gang@gmail.com")
                .phoneNumber("010-8888-6666")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.F)
                .birthDate("1979-02-02")
                .role(Role.USER)
                .build();

        return Stream.of(
                Arguments.of(memberRequestDto1),
                Arguments.of(memberRequestDto2),
                Arguments.of(memberRequestDto3),
                Arguments.of(memberRequestDto4)
        );
    }

    @Test
    @DisplayName("이메일이 null인 경우 테스트")
    @Order(9)
    void testMemberEmailNull() throws Exception {
        MemberSaveRequestDto memberRequestDto = MemberSaveRequestDto.builder()
                .userName("김주영")
                .account("help123")
                .password("*yjyjyjyj277227")
                .email(null)
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("2000-09-11")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("이메일은 필수 입력 항목입니다")));
    }

    @ParameterizedTest
    @MethodSource("validationMemberEmailGroup")
    @DisplayName("이메일 형식에 맞지 않는 경우")
    @Order(10)
    void testMemberEmailNotMatchedPattern(MemberSaveRequestDto memberRequestDto) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("올바른 메일 형식이 아닙니다")));
    }

    private static Stream<Arguments> validationMemberEmailGroup() {
        MemberSaveRequestDto memberRequestDto1 = MemberSaveRequestDto.builder()
                .userName("김주영")
                .account("user01")
                .password("*yjym2222345")
                .email("user01") // @ 없는 경우
                .phoneNumber("010-1111-4444")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("2002-01-11")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto2 = MemberSaveRequestDto.builder()
                .userName("김하늘")
                .account("user02")
                .password("*yjym2222345")
                .email("@gmail.com") // @부터 있는 경우
                .phoneNumber("010-1111-4444")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1950-04-23")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto3 = MemberSaveRequestDto.builder()
                .userName("노홍철")
                .account("user03")
                .password("*yjym2222345")
                .email("@") // @만 있는 경우
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1988-08-29")
                .role(Role.USER)
                .build();

        return Stream.of(
                Arguments.of(memberRequestDto1),
                Arguments.of(memberRequestDto2),
                Arguments.of(memberRequestDto3)
        );
    }

    @Test
    @DisplayName("휴대폰 번호가 null인 경우")
    @Order(11)
    void testMemberPhoneNumberNull() throws Exception {
        MemberSaveRequestDto memberRequestDto = MemberSaveRequestDto.builder()
                .userName("김주영")
                .account("help123")
                .password("*yjyjyjyj277227")
                .email("juyn@naver.com")
                .phoneNumber(null)
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1878-01-22")
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("휴대폰 번호는 필수 입력 항목입니다")));
    }

    @ParameterizedTest
    @MethodSource("validationMemberPhoneNumberGroup")
    @DisplayName("휴대폰 형식에 맞지 않는 경우")
    @Order(12)
    void testMemberPhoneNotMatchedPattern(MemberSaveRequestDto memberRequestDto) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("올바른 휴대폰번호 형식이 아닙니다")));
    }

    private static Stream<Arguments> validationMemberPhoneNumberGroup() {
        MemberSaveRequestDto memberRequestDto1 = MemberSaveRequestDto.builder()
                .userName("김주영")
                .account("user01")
                .password("*yjym2222345")
                .email("user01@naver.com")
                .phoneNumber("010-") // 중간, 마지막 번호 없는 경우
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("2002-01-11")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto2 = MemberSaveRequestDto.builder()
                .userName("김하늘")
                .account("user02")
                .password("*yjym2222345")
                .email("hanle@gmail.com")
                .phoneNumber("010-1111") // 마지막 번호 없는 경우
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1950-04-23")
                .role(Role.USER)
                .build();

        MemberSaveRequestDto memberRequestDto3 = MemberSaveRequestDto.builder()
                .userName("노홍철")
                .account("user03")
                .password("*yjym2222345")
                .email("junmin@gmail.com")
                .phoneNumber("010-2222-333") // 마지막 번호가 세자리 인 경우
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate("1988-08-29")
                .role(Role.USER)
                .build();

        return Stream.of(
                Arguments.of(memberRequestDto1),
                Arguments.of(memberRequestDto2),
                Arguments.of(memberRequestDto3)
        );
    }

    @Test
    @DisplayName("생년월일이 null인 경우")
    @Order(13)
    void testMemberBirthDate() throws Exception {
        MemberSaveRequestDto memberRequestDto = MemberSaveRequestDto.builder()
                .userName("김주영")
                .account("help123")
                .password("*yjyjyjyj277227")
                .email("juyn@naver.com")
                .phoneNumber("010-2222-3333")
                .certYn("Y")
                .accountCertYn("Y")
                .gender(Gender.M)
                .birthDate(null)
                .role(Role.USER)
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("생년월일은 필수 입력 항목입니다")));
    }
}
