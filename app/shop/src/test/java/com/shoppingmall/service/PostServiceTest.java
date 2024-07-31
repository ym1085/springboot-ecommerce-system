package com.shoppingmall.service;

import com.shoppingmall.dto.request.PostSaveRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.vo.PagingResponse;
import com.shoppingmall.mapper.CommentMapper;
import com.shoppingmall.mapper.PostFileMapper;
import com.shoppingmall.mapper.PostMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.vo.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    private final Logger logger = LoggerFactory.getLogger(PostServiceTest.class);

    @InjectMocks
    private PostService postService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostFileMapper postFileMapper;

    @Mock
    private FileHandlerHelper fileHandlerHelper;

    @Mock
    private CommentMapper commentMapper;

    private static int getRandom() {
        return new Random().nextInt(10) + 1;
    }

    @Test
    @DisplayName("난수 생성 테스트")
    void testRandom() {
        for (int i = 0; i < 50; i++) {
            int random = getRandom();
            System.out.println("[" + i + "] 생성된 난수 => " + random);
            assertTrue(random >= 1 && random <= 10, "난수 범위 오류: " + random);
        }
    }

    @Test
    @DisplayName("테스트 데이터 생성")
    void insertPostData() {
        List<Post> posts = postService.getPosts(new SearchRequestDto()).getData();
        if (CollectionUtils.isEmpty(posts)) {
            for (int i = 1; i <= 1000; i++) {
                PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
                postSaveRequestDto.setTitle("제목" + i);
                postSaveRequestDto.setContent("내용" + i);
                postSaveRequestDto.setMemberId(getRandom());
                postSaveRequestDto.setFixedYn("N");
                postSaveRequestDto.setCategoryId(getRandom());
                postMapper.savePost(postSaveRequestDto);
//                postService.savePost(postSaveRequestDto);
            }
        }
    }

    private static Stream<Arguments> is_get_multi_posts_params() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        Post.builder()
                                .postId(1)
                                .memberId(1)
                                .title("테스트 001")
                                .content("내용 001")
                                .categoryId(1)
                                .categoryName("일상/소통")
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
                                .categoryName("일상/소통")
                                .writer("장기하")
                                .readCnt(2)
                                .fixedYn("N")
                                .delYn("N")
                                .createDate(LocalDateTime.now())
                                .updateDate(LocalDateTime.now())
                                .postFiles(new ArrayList<>())
                                .build()
                        ,
                        Post.builder()
                                .postId(3)
                                .memberId(3)
                                .title("테스트 003")
                                .content("내용 003")
                                .categoryId(3)
                                .categoryName("반려/생활")
                                .writer("김지은")
                                .readCnt(3)
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
    @DisplayName("전체 게시글 조회 테스트")
    void getPosts(List<Post> mockPosts) {
        // given
        Mockito.when(postMapper.count(Mockito.any(SearchRequestDto.class))).thenReturn(mockPosts.size());
        Mockito.when(postMapper.getPosts(Mockito.any(SearchRequestDto.class))).thenReturn(mockPosts);

        // when
        PagingResponse<Post> paging = postService.getPosts(new SearchRequestDto());
        List<Post> posts = paging.getData();
        logger.info("posts = " + posts);
        logger.info("posts.size = " + posts.size());

        // then
        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSizeGreaterThan(1);
        assertThat(posts.size()).isEqualTo(3);
        assertThat(posts.get(0).getPostId()).isEqualTo(1L);
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
                                .categoryName("일상/소통")
                                .writer("아이유")
                                .readCnt(1)
                                .fixedYn("N")
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
    @DisplayName("단일 게시글 조회 테스트")
    void getPostById(Post mockPost) {
        // given
        Mockito.when(postMapper.increasePostByPostId(Mockito.any())).thenReturn(1);
        Mockito.when(postMapper.getPostByPostId(Mockito.any())).thenReturn(Optional.of(mockPost));

        // when
        Post post = postService.getPostById(mockPost.getPostId());

        // then
        assertThat(post).isNotNull();
        assertThat(post.getPostId()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource(value = "is_get_single_post_params")
    @DisplayName("게시글 등록 테스트")
    void savePost(Post mockPost) {
        // given
        Mockito.when(postMapper.increasePostByPostId(Mockito.any())).thenReturn(1);
        Mockito.when(postMapper.getPostByPostId(Mockito.any())).thenReturn(Optional.of(mockPost));
        Mockito.when(postMapper.savePost(Mockito.any())).thenReturn(1);

        // when
        PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
        postSaveRequestDto.setPostId(1);
        postSaveRequestDto.setMemberId(1);
        postSaveRequestDto.setCategoryId(1);
        postSaveRequestDto.setTitle("테스트 001");
        postSaveRequestDto.setContent("테스트 내용 001");
        postSaveRequestDto.setFixedYn("N");
        postSaveRequestDto.setFiles(new ArrayList<>());

        Integer postId = postService.savePost(postSaveRequestDto);
        logger.info("postId = {}", postId);

        Post result = postService.getPostById(postId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(1L);
    }
}
