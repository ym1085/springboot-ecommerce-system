package com.post.service.impl;

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
@DisplayName("게시판 테스트 클래스")
@Transactional // 트랜잭션 관리
@SpringBootTest // 통합 테스트
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
//        assertThat(posts.get(0).getPostId()).isEqualTo(2);
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
        System.out.println("posts => " + posts);

        //then
        assertThat(posts).isNotNull();
//        assertThat(posts).isNull();
        assertThat(posts.getPostId()).isEqualTo(1);
    }
}
