package com.shoppingmall.service;

import com.shoppingmall.mapper.CommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
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
        // given

        // when

        // then
    }

    @Test
    @DisplayName("부모 댓글이 없는 경우 테스트")
    void testSaveCommentNoneParentCommentId() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("댓글 삭제 테스트(부모, 자식 댓글 전부 삭제)")
    void testDeleteParentAndChildComments() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("대댓글 삭제 테스트")
    void testDeleteChildComments() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("commentId, parentId가 모두 공백인 경우 예외 발생 테스트")
    void testDeleteParentAndChildCommentsAllIdNull() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("댓글 내용 수정 테스트")
    void testUpdateCommentById() {
        // given

        // when

        // then
    }
}
