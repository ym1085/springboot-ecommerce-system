package com.post.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("전체 게시글 조회 API 컨트롤러 테스트")
    @Order(1)
    void getPosts() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                get("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].postId").isNotEmpty())
                .andExpect(jsonPath("$.[0].postId", equalTo(1)))
                .andExpect(jsonPath("$.[0].title", equalTo("제목1")))
                .andExpect(jsonPath("$.[0].content", equalTo("내용1")))
                .andExpect(jsonPath("$.[0].fixedYn", equalTo("N")));
    }

    @Test
    @DisplayName("단일 게시글 조회 API 컨트롤러 테스트")
    @Order(2)
    void getPostById() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                get("/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.postId", is(1)))
                .andExpect(jsonPath("$.title", is("제목1")))
                .andExpect(jsonPath("$.content", is("내용1")))
                .andExpect(jsonPath("$.fixedYn", is("N")));
    }

    @Test
    @DisplayName("게시글 등록 API 컨트롤러 테스트")
    @Order(3)
    void savePost() throws Exception {
        //given
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("postId", "1");
        paramMap.add("memberId", "1");
        paramMap.add("title", "제목1");
        paramMap.add("content", "내용1");
        paramMap.add("fixedYn", "N");

        //when
        ResultActions result = mockMvc.perform(
                post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(paramMap)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print());
    }
}
