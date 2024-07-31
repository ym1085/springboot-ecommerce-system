package com.shoppingmall.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.ShopApplication;
import com.shoppingmall.constant.Gender;
import com.shoppingmall.constant.Role;
import com.shoppingmall.dto.request.MemberSaveRequestDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@WithMockUser(username = "ymkim", roles = {"USER"})
@SpringBootTest(classes = ShopApplication.class)
class MemberRestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        String username = "admin";
        String password = "admin1234!";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UserDetails principal = new User(username, password, AuthorityUtils.createAuthorityList("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("회원 가입 성공")
    void testSignUp() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName("김영민");
        memberSaveRequestDto.setAccount("test01");
        memberSaveRequestDto.setPassword("yyyyjjj*1");
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("A0103")))
                .andExpect(jsonPath("$.message", Matchers.containsString("회원가입에 성공")));
    }

    @Test
    @DisplayName("이름이 null인 경우 테스트")
    void testMemberNameNull() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName(null);
        memberSaveRequestDto.setAccount("test01");
        memberSaveRequestDto.setPassword("yyyyjjj*1");
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("이름은 필수 입력 항목 입니다")));
    }

    @Test
    @DisplayName("이름이 한글자 미만인 경우 테스트")
    void testMemberNameLessThan1() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName("김");
        memberSaveRequestDto.setAccount("test01");
        memberSaveRequestDto.setPassword("yyyyjjj*1");
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요")));
    }

    @Test
    @DisplayName("이름이 여섯글자 초과인 경우 테스트")
    void testMemberNameOverThan6() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName("산다라박이무니다");
        memberSaveRequestDto.setAccount("test01");
        memberSaveRequestDto.setPassword("yyyyjjj*1");
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요")));
    }

    @Test
    @DisplayName("ID가 null인 경우 테스트")
    void testMemberIdNull() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName("산다라박이무니다");
        memberSaveRequestDto.setAccount(null);
        memberSaveRequestDto.setPassword("yyyyjjj*1");
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("ID는 필수 입력 항목 입니다")));
    }

    @Test
    @DisplayName("ID가 30자 이상인 경우 테스트 - 예외 발생")
    void testMemberIdOverThan30() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName("산다라박이무니다");
        memberSaveRequestDto.setAccount("aaaaaaaaaabbbbbbbbbbdddddddddd3");
        memberSaveRequestDto.setPassword("yyyyjjj*1");
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("30자 이하의 ID만 입력 가능합니다")));
    }

    @Test
    @DisplayName("비밀번호가 null인 경우")
    void testMemberPasswordNull() throws Exception {
        MemberSaveRequestDto memberSaveRequestDto = new MemberSaveRequestDto();
        memberSaveRequestDto.setName("산다라박이무니다");
        memberSaveRequestDto.setAccount("aaaaaaaaaabbbbbbbbbbdddddddddd3");
        memberSaveRequestDto.setPassword(null);
        memberSaveRequestDto.setEmail("test@gmail.com");
        memberSaveRequestDto.setPhoneNumber("010-2222-3333");
        memberSaveRequestDto.setCertYn("Y");
        memberSaveRequestDto.setAccountCertYn("Y");
        memberSaveRequestDto.setGender(Gender.M);
        memberSaveRequestDto.setBirthDate("1993-08-23");
        memberSaveRequestDto.setRole(Role.USER);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberSaveRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("비밀번호는 필수 입력 항목 입니다")));
    }

    private static Stream<Arguments> validationMemberPasswordGroup() {
        MemberSaveRequestDto memberRequestDto1 = new MemberSaveRequestDto();
        memberRequestDto1.setName("김주영");
        memberRequestDto1.setAccount("user01");
        memberRequestDto1.setPassword("sjsjsjsjs"); // 문자만 있는 경우
        memberRequestDto1.setEmail("user01@gmail.com");
        memberRequestDto1.setPhoneNumber("010-1111-4444");
        memberRequestDto1.setCertYn("Y");
        memberRequestDto1.setAccountCertYn("Y");
        memberRequestDto1.setGender(Gender.M);
        memberRequestDto1.setBirthDate("2002-01-11");

        MemberSaveRequestDto memberRequestDto2 = new MemberSaveRequestDto();
        memberRequestDto2.setName("김하늘");
        memberRequestDto2.setAccount("user02");
        memberRequestDto2.setPassword("23213213213"); // 숫자만 있는 경우
        memberRequestDto2.setEmail("hadmdm@gmail.com");
        memberRequestDto2.setPhoneNumber("010-1111-4444");
        memberRequestDto2.setCertYn("Y");
        memberRequestDto2.setAccountCertYn("Y");
        memberRequestDto2.setGender(Gender.M);
        memberRequestDto2.setBirthDate("1950-04-23");

        MemberSaveRequestDto memberRequestDto3 = new MemberSaveRequestDto();
        memberRequestDto3.setName("노홍철");
        memberRequestDto3.setAccount("user03");
        memberRequestDto3.setPassword("*&^%$#@!"); // 특수문자만 있는 경우
        memberRequestDto3.setEmail("hong@gmail.com");
        memberRequestDto3.setPhoneNumber("010-2222-3333");
        memberRequestDto3.setCertYn("Y");
        memberRequestDto3.setAccountCertYn("Y");
        memberRequestDto3.setGender(Gender.M);
        memberRequestDto3.setBirthDate("1988-08-29");

        MemberSaveRequestDto memberRequestDto4 = new MemberSaveRequestDto();
        memberRequestDto4.setName("강호동");
        memberRequestDto4.setAccount("user04");
        memberRequestDto4.setPassword("1234567"); // 8글자 미만인 경우
        memberRequestDto4.setEmail("gang@gmail.com");
        memberRequestDto4.setPhoneNumber("010-8888-6666");
        memberRequestDto4.setCertYn("Y");
        memberRequestDto4.setAccountCertYn("Y");
        memberRequestDto4.setGender(Gender.F);
        memberRequestDto4.setBirthDate("1979-02-02");

        return Stream.of(
                Arguments.of(memberRequestDto1),
                Arguments.of(memberRequestDto2),
                Arguments.of(memberRequestDto3),
                Arguments.of(memberRequestDto4)
        );
    }

    @ParameterizedTest
    @MethodSource("validationMemberPasswordGroup")
    @DisplayName("비밀번호 형식에 맞지 않는 경우")
    void testMemberPasswordNotMatchedPattern(MemberSaveRequestDto memberRequestDto) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요")));
    }

    @Test
    @DisplayName("이메일이 null인 경우 테스트")
    void testMemberEmailNull() throws Exception {
        MemberSaveRequestDto memberRequestDto = new MemberSaveRequestDto();
        memberRequestDto.setName("김주영");
        memberRequestDto.setAccount("help123");
        memberRequestDto.setPassword("*yjyjyjyj277227");
        memberRequestDto.setEmail(null);
        memberRequestDto.setPhoneNumber("010-2222-3333");
        memberRequestDto.setCertYn("Y");
        memberRequestDto.setAccountCertYn("Y");
        memberRequestDto.setGender(Gender.M);
        memberRequestDto.setBirthDate("2000-09-11");

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("이메일은 필수 입력 항목입니다")));
    }

    private static Stream<Arguments> validationMemberEmailGroup() {
        MemberSaveRequestDto memberRequestDto1 = new MemberSaveRequestDto();
        memberRequestDto1.setName("김주영");
        memberRequestDto1.setAccount("user01");
        memberRequestDto1.setPassword("*yjym2222345");
        memberRequestDto1.setEmail("user01"); // @ 없는 경우
        memberRequestDto1.setPhoneNumber("010-1111-4444");
        memberRequestDto1.setCertYn("Y");
        memberRequestDto1.setAccountCertYn("Y");
        memberRequestDto1.setGender(Gender.M);
        memberRequestDto1.setBirthDate("2002-01-11");

        MemberSaveRequestDto memberRequestDto2 = new MemberSaveRequestDto();
        memberRequestDto2.setName("김하늘");
        memberRequestDto2.setAccount("user02");
        memberRequestDto2.setPassword("*yjym2222345");
        memberRequestDto2.setEmail("@gmail.com"); // @부터 있는 경우
        memberRequestDto2.setPhoneNumber("010-1111-4444");
        memberRequestDto2.setCertYn("Y");
        memberRequestDto2.setAccountCertYn("Y");
        memberRequestDto2.setGender(Gender.M);
        memberRequestDto2.setBirthDate("1950-04-23");

        MemberSaveRequestDto memberRequestDto3 = new MemberSaveRequestDto();
        memberRequestDto3.setName("노홍철");
        memberRequestDto3.setAccount("user03");
        memberRequestDto3.setPassword("*yjym2222345");
        memberRequestDto3.setEmail("@"); // @만 있는 경우
        memberRequestDto3.setPhoneNumber("010-2222-3333");
        memberRequestDto3.setCertYn("Y");
        memberRequestDto3.setAccountCertYn("Y");
        memberRequestDto3.setGender(Gender.M);
        memberRequestDto3.setBirthDate("1988-08-29");

        return Stream.of(
                Arguments.of(memberRequestDto1),
                Arguments.of(memberRequestDto2),
                Arguments.of(memberRequestDto3)
        );
    }

    @ParameterizedTest
    @MethodSource("validationMemberEmailGroup")
    @DisplayName("이메일 형식에 맞지 않는 경우")
    void testMemberEmailNotMatchedPattern(MemberSaveRequestDto memberRequestDto) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("올바른 메일 형식이 아닙니다")));
    }

    @Test
    @DisplayName("휴대폰 번호가 null인 경우")
    void testMemberPhoneNumberNull() throws Exception {
        MemberSaveRequestDto memberRequestDto = new MemberSaveRequestDto();
        memberRequestDto.setName("김주영");
        memberRequestDto.setAccount("help123");
        memberRequestDto.setPassword("*yjyjyjyj277227");
        memberRequestDto.setEmail("juyn@naver.com");
        memberRequestDto.setPhoneNumber(null);
        memberRequestDto.setCertYn("Y");
        memberRequestDto.setAccountCertYn("Y");
        memberRequestDto.setGender(Gender.M);
        memberRequestDto.setBirthDate("1878-01-22");

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("휴대폰 번호는 필수 입력 항목입니다")));
    }

    private static Stream<Arguments> validationMemberPhoneNumberGroup() {
        MemberSaveRequestDto memberRequestDto1 = new MemberSaveRequestDto();
        memberRequestDto1.setName("김주영");
        memberRequestDto1.setAccount("user01");
        memberRequestDto1.setPassword("*yjym2222345");
        memberRequestDto1.setEmail("user01@naver.com");
        memberRequestDto1.setPhoneNumber("010-"); // 중간, 마지막 번호 없는 경우
        memberRequestDto1.setCertYn("Y");
        memberRequestDto1.setAccountCertYn("Y");
        memberRequestDto1.setGender(Gender.M);
        memberRequestDto1.setBirthDate("2002-01-11");

        MemberSaveRequestDto memberRequestDto2 = new MemberSaveRequestDto();
        memberRequestDto2.setName("김하늘");
        memberRequestDto2.setAccount("user02");
        memberRequestDto2.setPassword("*yjym2222345");
        memberRequestDto2.setEmail("hanle@gmail.com");
        memberRequestDto2.setPhoneNumber("010-1111"); // 마지막 번호 없는 경우
        memberRequestDto2.setCertYn("Y");
        memberRequestDto2.setAccountCertYn("Y");
        memberRequestDto2.setGender(Gender.M);
        memberRequestDto2.setBirthDate("1950-04-23");

        MemberSaveRequestDto memberRequestDto3 = new MemberSaveRequestDto();
        memberRequestDto3.setName("노홍철");
        memberRequestDto3.setAccount("user03");
        memberRequestDto3.setPassword("*yjym2222345");
        memberRequestDto3.setEmail("junmin@gmail.com");
        memberRequestDto3.setPhoneNumber("010-2222-333"); // 마지막 번호가 세자리 인 경우
        memberRequestDto3.setCertYn("Y");
        memberRequestDto3.setAccountCertYn("Y");
        memberRequestDto3.setGender(Gender.M);
        memberRequestDto3.setBirthDate("1988-08-29");

        return Stream.of(
                Arguments.of(memberRequestDto1),
                Arguments.of(memberRequestDto2),
                Arguments.of(memberRequestDto3)
        );
    }

    @ParameterizedTest
    @MethodSource("validationMemberPhoneNumberGroup")
    @DisplayName("휴대폰 형식에 맞지 않는 경우")
    void testMemberPhoneNotMatchedPattern(MemberSaveRequestDto memberRequestDto) throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("올바른 휴대폰번호 형식이 아닙니다")));
    }

    @Test
    @DisplayName("생년월일이 null인 경우")
    void testMemberBirthDate() throws Exception {
        MemberSaveRequestDto memberRequestDto = new MemberSaveRequestDto();
        memberRequestDto.setName("김주영");
        memberRequestDto.setAccount("help123");
        memberRequestDto.setPassword("*yjyjyjyj277227");
        memberRequestDto.setEmail("juyn@naver.com");
        memberRequestDto.setPhoneNumber("010-2222-3333");
        memberRequestDto.setCertYn("Y");
        memberRequestDto.setAccountCertYn("Y");
        memberRequestDto.setGender(Gender.M);
        memberRequestDto.setBirthDate(null);

        ResultActions result = mockMvc.perform(
                post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberRequestDto))
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo("400")))
                .andExpect(jsonPath("$.message", Matchers.containsString("유효하지 않은 파라미터가 포함")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("생년월일은 필수 입력 항목입니다")));
    }
}
