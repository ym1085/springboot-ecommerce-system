package com.post.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.repository.post.FileMapper;
import com.post.repository.post.PostMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostApiControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FileMapper fileMapper;

    @Test
    @DisplayName("전체 게시글 조회 API")
    @Order(1)
    void getPosts() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                get("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.result[0].postId").isNotEmpty())
                .andExpect(jsonPath("$.data.result[0].postId", equalTo(1003)))
                .andExpect(jsonPath("$.data.result[0].title", equalTo("제목1003")))
                .andExpect(jsonPath("$.data.result[0].content", equalTo("내용1003")))
                .andExpect(jsonPath("$.data.result[0].fixedYn", equalTo("N")));
    }

    @Test
    @DisplayName("단일 게시글 조회 API")
    @Order(2)
    void getPostById() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                get("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.postId").isNotEmpty())
                .andExpect(jsonPath("$.data.postId", is(1)))
                .andExpect(jsonPath("$.data.title", is("제목1")))
                .andExpect(jsonPath("$.data.content", is("내용1")))
                .andExpect(jsonPath("$.data.fixedYn", is("N")));
    }

    @Test
    @DisplayName("게시글 등록 API")
    @Order(3)
    void  Post() throws Exception {
        //given
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("postId", "1");
        paramMap.add("memberId", "1");
        paramMap.add("title", "제목1");
        paramMap.add("content", "내용1");
        paramMap.add("fixedYn", "N");

        //when
        ResultActions result = mockMvc.perform(
                post("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(paramMap)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 API - 제목 공백인 경우 예외 발생")
    void uploadPostTitleNull() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        //then
        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.message", Matchers.containsString("제목은 반드시 입력되어야 합니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 내용 공백인 경우 예외 발생")
    void uploadPostContentNull() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "제목1")
                        .param("content", "")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
        );

        //then
        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.message", Matchers.containsString("내용은 반드시 입력되어야 합니다.")));
    }

    @Test
    @DisplayName("게시글 수정 API - 제목이 20을 초과하는 경우 예외 발생")
    void uploadPostTitleOverThan20() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "제목1제목1제목1제목1제목1제목1제목1")
                        .param("content", "내용1")
                        .param("fixedYn", "")
                        .param("memberId", "1")
        );

        //then
        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.message", Matchers.containsString("제목은 20자를 초과할 수 없습니다.")));
    }

    @Test
    @DisplayName("게시글 수정 API - 내용이 250자를 넘는 경우 경우 예외 발생")
    void uploadPostContentOverThan250() throws Exception {
        //given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 251; i++) {
            sb.append("a");
        }

        //when
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "제목1")
                        .param("content", sb.toString())
                        .param("fixedYn", "")
                        .param("memberId", "1")
        );

        //then
        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.message", Matchers.containsString("내용은 250자를 초과할 수 없습니다.")));
    }

    @Test
    @DisplayName("게시글 삭제 API 테스트")
    void deletePost() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                delete("/api/v1/post/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print());

        assertThat(postMapper.getPostById(1L)).isEmpty();
        assertThat(fileMapper.getFiles(1L)).isEmpty();
    }
}
