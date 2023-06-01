package com.multi.posts.service.impl;

import com.multi.posts.domain.Comment;
import com.multi.posts.dto.request.CommentRequestDto;
import com.multi.posts.repository.CommentMapper;
import com.multi.posts.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Test
    @DisplayName("댓글 + 대댓글 저장 테스트")
    void testSaveComment() {
        Comment comment = new Comment(CommentRequestDto.builder()
                .parentId(14L)
                .postId(20L)
                .content("댓글 테스트")
                .memberId(1L)
                .build());

        //when
        int result = commentMapper.saveComment(comment);

        //then
        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("댓글 + 대댓글 저장 테스트 - 최상위 부모 댓글이 없는 경우")
    void testSaveCommentNoneParentCommentId() {
        //given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .parentId(999999L) // 존재하지 않는 commentId
                .postId(20L)
                .content("댓글 테스트")
                .memberId(1L)
                .build();

        //when
        int result = commentService.saveComment(commentRequestDto);
        System.out.println("result ===> " + result);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(0); // fail
    }

    @Test
    @DisplayName("댓글 + 대댓글 삭제 테스트 -> 부모, 자식 댓글들이 전부 삭제 되어야 함")
    void testDeleteParentAndChildComments() {
        // 재귀 사용해서 지우고 있음
        Comment comment = new Comment(CommentRequestDto.builder()
                .commentId(14L) // 댓글
                .parentId(14L) // 대댓글
                .postId(20L)
                .memberId(1L)
                .build());

        //when
        int result = commentMapper.deleteCommentByCommentIdAndParentId(comment);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("대댓글 삭제 테스트 - 특정 대댓글 1개만 삭제 되어야함")
    void testDeleteChildComments() {
        //given
        Comment comment = new Comment(CommentRequestDto.builder()
                .commentId(14L) // 대댓글 삭제하는 경우 parentId -> commentId에 셋팅 후 서버에 넘겨서 삭제할 예정
                .postId(20L)
                .memberId(1L)
                .build());

        //when
        int result = commentMapper.deleteCommentByCommentId(comment);

        //then
        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("댓글 + 대댓글 삭제 테스트 - 댓글 ID가 전부 공백인 경우 -> 예외 상황")
    void testDeleteParentAndChildCommentsAllIdNull() {
        //given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .commentId(null)
                .parentId(null)
                .postId(20L)
                .memberId(1L)
                .build();

        //when
        int result = commentService.deleteCommentById(commentRequestDto);

        //then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("자식 댓글이 있는 상위 댓글을 삭제 하는 경우 -> 예외 상황")
    void testDeleteCommentNoneChildCommentId() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .commentId(14L) // 14번 부모 댓글은 15, 16 자식 댓글이 있다고 가정
                .parentId(null) // 하위 대댓글 번호가 있어야 재귀를 통해 싹다 삭제를 한다
                .postId(20L)
                .memberId(1L)
                .build();

        //when
        int result = commentService.deleteCommentById(commentRequestDto);

        //then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 수정")
    void testUpdateCommentById() {
        Comment comment = new Comment(CommentRequestDto.builder()
                .commentId(14L) // 대댓글 삭제하는 경우 parentId -> commentId에 셋팅 후 서버에 넘겨서 삭제할 예정
                .content("댓글 수정을 해보이겠소")
                .build());

        //when
        int result = commentMapper.updateCommentById(comment);

        //then
        assertThat(result).isGreaterThan(0);
    }
}
