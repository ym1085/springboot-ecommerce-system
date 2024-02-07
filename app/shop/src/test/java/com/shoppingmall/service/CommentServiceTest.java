package com.shoppingmall.service;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.CommentDeleteRequestDto;
import com.shoppingmall.dto.request.CommentSaveRequestDto;
import com.shoppingmall.dto.request.CommentUpdateRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.exception.FailDeleteCommentException;
import com.shoppingmall.mapper.CommentMapper;
import com.shoppingmall.vo.CommentVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(classes = ShopApplication.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

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

    @Test
    @DisplayName("댓글 및 대대글 저장 테스트")
    void testSaveComment() {
        CommentVO comment = CommentSaveRequestDto.builder()
                .parentId(14L)
                .postId(20L)
                .content("댓글 테스트")
                .memberId(1L)
                .build()
                .toEntity();

        int result = commentMapper.saveComment(comment);

        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("부모 댓글이 없는 경우 테스트")
    void testSaveCommentNoneParentCommentId() {
        CommentSaveRequestDto commentRequestDto = CommentSaveRequestDto.builder()
                .parentId(999999L) // 존재하지 않는 commentId
                .postId(20L)
                .content("댓글 테스트")
                .memberId(1L)
                .build();

        List<CommentResponseDto> comments = commentService.saveComment(commentRequestDto);

        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 삭제 테스트(부모, 자식 댓글 전부 삭제)")
    void testDeleteParentAndChildComments() {
        CommentVO comment = CommentSaveRequestDto.builder()
                .commentId(6L) // 댓글 ID
                .parentId(6L) // 부모 댓글 ID
                .postId(20L)
                .memberId(1L)
                .build()
                .toEntity();

        int result = commentMapper.deleteComment(comment);

        assertThat(result).isNotNull();
        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("대댓글 삭제 테스트")
    void testDeleteChildComments() {
        CommentVO comment = CommentSaveRequestDto.builder()
                .commentId(7L) // 대댓글 삭제하는 경우 parentId -> commentId에 셋팅 후 서버에 넘겨서 삭제할 예정
                .postId(20L)
                .memberId(1L)
                .build()
                .toEntity();

        int result = commentMapper.deleteCommentReply(comment);

        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("commentId, parentId가 모두 공백인 경우 예외 발생 테스트")
    void testDeleteParentAndChildCommentsAllIdNull() {
        CommentDeleteRequestDto commentRequestDto = CommentDeleteRequestDto.builder()
                .commentId(null)
                .postId(20L)
                .build();

        assertThrows(FailDeleteCommentException.class, () -> {
            commentService.deleteComments(commentRequestDto);
        });
    }

    @Test
    @DisplayName("댓글 내용 수정 테스트")
    void testUpdateCommentById() {
        CommentVO comment = CommentUpdateRequestDto.builder()
                .commentId(6L)
                .content("댓글 내용 수정 테스트")
                .build()
                .toEntity();

        int result = commentMapper.updateCommentByCommentId(comment);

        assertThat(result).isGreaterThan(0);
    }
}
