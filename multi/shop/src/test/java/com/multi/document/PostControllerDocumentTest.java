//package com.post.document;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.post.web.PostController;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(RestDocumentationExtension.class) // JUnit5 필수
//@WebMvcTest(PostController.class) // Presentation Layer Test를 위함
//@AutoConfigureRestDocs(uriScheme = "http", uriHost = "127.0.0.1", uriPort = 8080)
//@AutoConfigureMockMvc
//class PostControllerDocumentTest {
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    /*@MockBean
//    private PostService postService;
//
//    @MockBean
//    private FileService fileService;
//
//    @MockBean
//    private FileUtils fileUtils;*/
//
//    /*@BeforeEach
//    void setUp(WebApplicationContext wc, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(wc)
////                .apply(springSecurity()) // security 적용하면 넣어야 한다고 한다
//                .apply(documentationConfiguration(restDocumentation)).build();
//    }*/
//
//    @Test
//    @DisplayName("전체 게시글 조회 API - Docs")
//    void getPosts() throws Exception {
//        //given
//        //when
//        ResultActions result = mockMvc.perform(
//                get("/post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        result.andExpect(status().isOk())
//                .andDo(print())
//                .andDo(document("get-posts",
//                    /*requestParameters(
//                            parameterWithName("user").description("유저 이름").optional()
//                    ),*/
//                    responseFields(
//                        fieldWithPath("data.result[0].postId").description("게시글 번호"),
//                        fieldWithPath("data.result[0].title").description("게시글 제목"),
//                        fieldWithPath("data.result[0].content").description("게시글 내용"),
//                        fieldWithPath("data.result[0].fixedYn").description("고정글 지정 여부")
//                    ))
//                );
//    }
//}
