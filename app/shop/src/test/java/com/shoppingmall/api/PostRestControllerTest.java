package com.shoppingmall.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.ShopApplication;
import com.shoppingmall.mapper.FileMapper;
import com.shoppingmall.mapper.PostMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = ShopApplication.class)
class PostRestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FileMapper fileMapper;

    @BeforeEach
    public void setup() {
        String username = "admin";
        String password = "Funin0302!@#$%$";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UserDetails principal = new User(username, password, AuthorityUtils.createAuthorityList("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("전체 게시글 조회 API")
    void getPosts() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.data[0].postId").isNotEmpty())
//                .andExpect(jsonPath("$.data.result[0].postId", equalTo(1003))) // 값이 계속 변경되는데.. 흐음..
//                .andExpect(jsonPath("$.data.result[0].title", equalTo("제목1003")))
//                .andExpect(jsonPath("$.data.result[0].content", equalTo("내용1003")))
                .andExpect(jsonPath("$.result.data[0].fixedYn", equalTo("N")));
    }

    @Test
    @DisplayName("단일 게시글 조회 API")
    void getPostById() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/v1/post/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.postId").isNotEmpty())
                .andExpect(jsonPath("$.result.postId", is(2)))
                .andExpect(jsonPath("$.result.title", is("제목2")))
                .andExpect(jsonPath("$.result.content", is("내용2")))
                .andExpect(jsonPath("$.result.fixedYn", is("N")));
    }

    @Test
    @DisplayName("게시글 등록 API")
    void savePost() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/v1/post")
                        .param("title", "게시글1")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 API - 제목 공백인 경우 예외 발생")
    void updatePostTitleIsNull() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("Invalid parameter included")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("제목은 반드시 입력되어야 합니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 내용 공백인 경우 예외 발생")
    void updatePostContentIsNull() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "제목1")
                        .param("content", "")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("Invalid parameter included")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("내용은 반드시 입력되어야 합니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 제목이 20을 초과하는 경우 예외 발생")
    void updatePostTitleOverThan20() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "제목1제목1제목1제목1제목1제목1제목1제목제목제목")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("Invalid parameter included")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("제목은 20자를 초과할 수 없습니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 내용이 1000자를 넘는 경우 경우 예외 발생")
    void updatePostContentOverThan250() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2000; i++) { // 2000자
            sb.append("A");
        }

        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "제목1")
                        .param("content", sb.toString())
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("Invalid parameter included")))
                .andExpect(jsonPath("$.errors.[0].reason", Matchers.containsString("내용은 1000자를 초과할 수 없습니다")));
    }

    @Test
    @DisplayName("게시글 삭제 API 테스트")
    void deletePost() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/v1/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(print());

        assertThat(postMapper.getPostByPostId(1L)).isEmpty();
        assertThat(fileMapper.getFilesByPostId(1L)).isEmpty();
    }
}
