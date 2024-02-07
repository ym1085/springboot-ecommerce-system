package com.shoppingmall.service;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.PostSaveRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@TestPropertySource(locations = "classpath:application-test.yaml")
@Transactional
@SpringBootTest(classes = ShopApplication.class)
class PostServiceTest {

    @Autowired
    PostService postService;

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
        List<PostResponseDto> posts = postService.getPosts(new SearchRequestDto()).getData();
        if (CollectionUtils.isEmpty(posts)) {
            for (int i = 1; i <= 1000; i++) {
                PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
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
        PagingResponseDto<PostResponseDto> pagingResponseDto = postService.getPosts(new SearchRequestDto());
        List<PostResponseDto> posts = pagingResponseDto.getData();

        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSizeGreaterThan(1);
    }

    @ParameterizedTest
    @ValueSource(longs = 2L)
    @DisplayName("단일 게시글 조회 테스트")
    void getPostById(Long input) {
        PostResponseDto posts = postService.getPostById(input);

        assertThat(posts).isNotNull();
        //assertThat(posts).isNull();
        assertThat(posts.getPostId()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void savePost() {
        PostSaveRequestDto postRequestDto = new PostSaveRequestDto();
        postRequestDto.setPostId(1L);
        postRequestDto.setMemberId(1L);
        postRequestDto.setTitle("제목1001");
        postRequestDto.setContent("내용1001");
        postRequestDto.setFixedYn("N");

        Long postId = postService.savePost(postRequestDto);
        PostResponseDto result = postService.getPostById(postId);

        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo("제목1001");
        assertThat(result.getContent()).isEqualTo("내용1001");
        assertThat(result.getFixedYn()).isEqualTo("N");
    }
}
