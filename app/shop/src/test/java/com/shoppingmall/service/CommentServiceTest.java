package com.shoppingmall.service;

import com.shoppingmall.mapper.CommentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

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
