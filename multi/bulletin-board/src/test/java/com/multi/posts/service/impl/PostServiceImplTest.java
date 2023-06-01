package com.multi.posts.service.impl;

import com.multi.posts.dto.request.PostRequestDto;
import com.multi.posts.dto.request.SearchRequestDto;
import com.multi.posts.dto.resposne.PagingResponseDto;
import com.multi.posts.dto.resposne.PostResponseDto;
import com.multi.posts.service.impl.FileServiceImpl;
import com.multi.posts.service.impl.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("난수 생성 테스트")
    void testRandom() {
        for (int i = 0; i < 50; i++) {
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
                PostRequestDto postSaveRequestDto = new PostRequestDto();
                postSaveRequestDto.setTitle("제목" + i);
                postSaveRequestDto.setContent("내용" + i);
                postSaveRequestDto.setMemberId((long) getRandom());
                postSaveRequestDto.setFixedYn("N");
                postSaveRequestDto.setCategoryId(getRandom());
                postService.savePost(postSaveRequestDto);
            }
        }
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    void getPosts() {
        //given
        //when
        PagingResponseDto<PostResponseDto> pagingResponseDto = postService.getPosts(new SearchRequestDto());
        List<PostResponseDto> posts = pagingResponseDto.getResult();

        //then
        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSizeGreaterThan(1);
        assertThat(posts.get(0).getPostId()).isEqualTo(1003);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목1003");
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    @DisplayName("단일 게시글 조회 테스트")
    void getPostById(Long input) {
        //given
        //when
        PostResponseDto posts = postService.getPostById(1L);

        //then
        assertThat(posts).isNotNull();
        //assertThat(posts).isNull();
        assertThat(posts.getPostId()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void savePost() {
        //given
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setPostId(1L);
        postRequestDto.setMemberId(1L);
        postRequestDto.setTitle("제목1001");
        postRequestDto.setContent("내용1001");
        postRequestDto.setFixedYn("N");

        //when
        Long postId = postService.savePost(postRequestDto);
        PostResponseDto result = postService.getPostById(postId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo("제목1001");
        assertThat(result.getContent()).isEqualTo("내용1001");
        assertThat(result.getFixedYn()).isEqualTo("N");
    }
}
