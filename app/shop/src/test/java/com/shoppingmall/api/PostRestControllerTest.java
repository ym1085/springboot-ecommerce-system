package com.shoppingmall.api;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.security.annotation.WithMockCustomUser;
import com.shoppingmall.service.PostService;
import com.shoppingmall.vo.Post;
import com.shoppingmall.vo.response.PagingResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시판 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostRestController.class)
@ContextConfiguration(classes = { ShopApplication.class })
@WithMockUser(username = "ymkim", roles = "USER")
class PostRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private static Stream<Arguments> is_get_multi_posts_params() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        Post.builder()
                                .postId(1)
                                .memberId(1)
                                .title("테스트 001")
                                .content("내용 001")
                                .categoryId(1)
                                .categoryName("전체게시판")
                                .writer("아이유")
                                .readCnt(1)
                                .fixedYn("N")
                                .delYn("N")
                                .createDate(LocalDateTime.now())
                                .updateDate(LocalDateTime.now())
                                .postFiles(new ArrayList<>())
                                .build()
                        ,
                        Post.builder()
                                .postId(2)
                                .memberId(2)
                                .title("테스트 002")
                                .content("내용 002")
                                .categoryId(2)
                                .categoryName("전체게시판")
                                .writer("장기하")
                                .readCnt(2)
                                .fixedYn("N")
                                .delYn("N")
                                .createDate(LocalDateTime.now())
                                .updateDate(LocalDateTime.now())
                                .postFiles(new ArrayList<>())
                                .build()
                ))
        );
    }

    @ParameterizedTest
    @MethodSource(value = "is_get_multi_posts_params")
    @DisplayName("전체 게시글 조회 API")
    @Order(1)
    void getPostsTest(List<Post> mockPosts) throws Exception {
        // given
        PagingResponse<Post> mockPagingResponse = new PagingResponse<>();
        mockPagingResponse.setData(mockPosts);

        when(postService.getPosts(any(SearchRequestDto.class))).thenReturn(mockPagingResponse);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.data[0].postId").isNotEmpty())
                .andExpect(jsonPath("$.result.data[0].postId", equalTo(1)))
                .andExpect(jsonPath("$.result.data[0].title", equalTo("테스트 001")))
                .andExpect(jsonPath("$.result.data[0].content", equalTo("내용 001")))
                .andExpect(jsonPath("$.result.data[0].categoryId", equalTo(1)))
                .andExpect(jsonPath("$.result.data[0].categoryName", equalTo("전체게시판")))
                .andExpect(jsonPath("$.result.data[0].fixedYn", equalTo("N")));
    }

    private static Stream<Arguments> is_get_single_post_params() {
        return Stream.of(
                Arguments.of(
                        Post.builder()
                                .postId(1)
                                .memberId(1)
                                .title("테스트 001")
                                .content("내용 001")
                                .categoryId(1)
                                .categoryName("전체게시판")
                                .writer("아이유")
                                .readCnt(1)
                                .fixedYn("Y")
                                .delYn("N")
                                .createDate(LocalDateTime.now())
                                .updateDate(LocalDateTime.now())
                                .postFiles(new ArrayList<>())
                                .comments(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "is_get_single_post_params")
    @DisplayName("단일 게시글 조회 API")
    @Order(2)
    void getPostById(Post mockPost) throws Exception {
        // given
        when(postService.getPostById(any(Integer.class))).thenReturn(mockPost);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.result.postId").isNotEmpty())
                .andExpect(jsonPath("$.result.postId", is(1)))
                .andExpect(jsonPath("$.result.title", is("테스트 001")))
                .andExpect(jsonPath("$.result.content", is("내용 001")))
                .andExpect(jsonPath("$.result.categoryId", equalTo(1)))
                .andExpect(jsonPath("$.result.categoryName", equalTo("전체게시판")))
                .andExpect(jsonPath("$.result.fixedYn", equalTo("Y")));
    }

    @Test
    @DisplayName("게시글 등록 API")
    @WithMockCustomUser(username = "admin", roles = "USER")
    @Order(3)
    void savePost() throws Exception {
        // given
        // when
        ResultActions result = mockMvc.perform(
                post("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "게시글1")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
                        .with(csrf())
        );

        // then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 API - 제목 공백인 경우 예외 발생")
    @WithMockCustomUser(username = "admin", roles = "USER")
    @Order(4)
    void updatePostTitleIsNull() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("제목은 반드시 입력되어야 합니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 내용 공백인 경우 예외 발생")
    @WithMockCustomUser(username = "admin", roles = "USER")
    @Order(5)
    void updatePostContentIsNull() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "제목1")
                        .param("content", "")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("내용은 반드시 입력되어야 합니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 제목이 20을 초과하는 경우 예외 발생")
    @WithMockCustomUser(username = "admin", roles = "USER")
    @Order(6)
    void updatePostTitleOverThan20() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/api/v1/post/{postId}", 1L)
                        .param("title", "제목1제목1제목1제목1제목1제목1제목1제목제목제목")
                        .param("content", "내용1")
                        .param("fixedYn", "N")
                        .param("memberId", "1")
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("제목은 20자를 초과할 수 없습니다")));
    }

    @Test
    @DisplayName("게시글 수정 API - 내용이 1000자를 넘는 경우 경우 예외 발생")
    @WithMockCustomUser(username = "admin", roles = "USER")
    @Order(7)
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
                        .with(csrf())
        );

        result.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(400)))
                .andExpect(jsonPath("$.message", Matchers.containsString("내용은 1000자를 초과할 수 없습니다")));
    }

    @Test
    @DisplayName("게시글 삭제 API 테스트")
    @Order(8)
    void deletePost() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/v1/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", equalTo(200)))
                .andExpect(jsonPath("$.message", containsString("게시글 삭제에 성공하였습니다")));
    }
}
