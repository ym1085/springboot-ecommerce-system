package com.shoppingmall.service;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.vo.Comment;
import com.shoppingmall.dto.request.CommentRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.exception.FailDeleteCommentException;
import com.shoppingmall.mapper.CommentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    @DisplayName("댓글 및 대대글 저장 테스트")
    void testSaveComment() {
        Comment comment = new Comment(CommentRequestDto.builder()
                .parentId(14L)
                .postId(20L)
                .content("댓글 테스트")
                .memberId(1L)
                .build());

        int result = commentMapper.saveComment(comment);

        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("부모 댓글이 없는 경우 테스트")
    void testSaveCommentNoneParentCommentId() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
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
        Comment comment = new Comment(CommentRequestDto.builder()
                .commentId(6L) // 댓글 ID
                .parentId(6L) // 부모 댓글 ID
                .postId(20L)
                .memberId(1L)
                .build());

        int result = commentMapper.deleteComment(comment);

        assertThat(result).isNotNull();
        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("대댓글 삭제 테스트")
    void testDeleteChildComments() {
        Comment comment = new Comment(CommentRequestDto.builder()
                .commentId(7L) // 대댓글 삭제하는 경우 parentId -> commentId에 셋팅 후 서버에 넘겨서 삭제할 예정
                .postId(20L)
                .memberId(1L)
                .build());

        int result = commentMapper.deleteCommentReply(comment);

        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("commentId, parentId가 모두 공백인 경우 예외 발생 테스트")
    void testDeleteParentAndChildCommentsAllIdNull() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .commentId(null)
                .parentId(null)
                .postId(20L)
                .memberId(1L)
                .build();

        assertThrows(FailDeleteCommentException.class, () -> {
            commentService.deleteComments(commentRequestDto);
        });
    }

    @Test
    @DisplayName("댓글 내용 수정 테스트")
    void testUpdateCommentById() {
        Comment comment = new Comment(CommentRequestDto.builder()
                .commentId(6L)
                .content("댓글 내용 수정 테스트")
                .build());

        int result = commentMapper.updateCommentByCommentId(comment);

        assertThat(result).isGreaterThan(0);
    }
}
