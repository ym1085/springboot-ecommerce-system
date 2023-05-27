package com.post.service.impl;

import com.post.web.dto.request.PostRequestDto;
import com.post.web.dto.request.SearchRequestDto;
import com.post.web.dto.resposne.PagingResponseDto;
import com.post.web.dto.resposne.PostResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostServiceImpl postService;

    @Autowired
    private FileServiceImpl fileService;

    private static int getRandom() {
        return new Random().nextInt(10) + 1;
    }

    @Test
    @DisplayName("post 테이블에 회원 ID를 난수로 생성하기 위해 테스트")
    void testRandom() {
        for (int i = 0; i < 100; i++) {
            int random = getRandom();
            System.out.println("생성된 난수 => " + random);
            assertTrue(random >= 1 && random <= 10, "난수 범위 오류: " + random);
        }
    }

    @Test
    @DisplayName("테스트 데이터 생성")
    @Rollback(false)
    void insertPostData() {
        List<PostResponseDto> posts = postService.getPosts(new SearchRequestDto()).getResult();
        if (CollectionUtils.isEmpty(posts)) {
            for (int i = 1; i <= 1000; i++) {
                PostRequestDto postRequestDto = PostRequestDto.builder()
                        .title(i + "번 게시글 제목")
                        .content(i + "번 게시글 내용")
                        .memberId((long) getRandom())
                        .fixedYn("N")
                        .build();

                postService.savePost(postRequestDto);
            }
        }
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    @Order(1)
    void getPosts() {
        //given
        //when
        PagingResponseDto<PostResponseDto> pagingResponseDto = postService.getPosts(new SearchRequestDto());
        List<PostResponseDto> posts = pagingResponseDto.getResult();

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
