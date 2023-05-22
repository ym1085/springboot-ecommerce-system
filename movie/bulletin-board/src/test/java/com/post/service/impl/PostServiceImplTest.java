package com.post.service.impl;

import com.post.web.dto.request.PostRequestDto;
import com.post.web.dto.resposne.PostResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostServiceImpl postService;

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    @Order(1)
    void getPosts() {
        //given
        //when
        List<PostResponseDto> posts = postService.getPosts();

        //then
        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSizeGreaterThan(10);
        //assertThat(posts.get(0).getPostId()).isEqualTo(2);
        assertThat(posts.get(0).getPostId()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목1");
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    @DisplayName("단일 게시글 조회 테스트")
    @Order(2)
    void getPostById(Long input) {
        //given
        //when
        PostResponseDto posts = postService.getPostById(input);

        //then
        assertThat(posts).isNotNull();
        //assertThat(posts).isNull();
        assertThat(posts.getPostId()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    @Order(3)
    void savePost() {
        //given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .postId(1L)
                .memberId(1L)
                .title("제목1")
                .content("내용1")
                .fixedYn("N")
                .build();

        //when
        Long postId = postService.savePost(postRequestDto);
        PostResponseDto result = postService.getPostById(postId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo("제목1");
        assertThat(result.getContent()).isEqualTo("내용1");
        assertThat(result.getFixedYn()).isEqualTo("N");
    }
}
