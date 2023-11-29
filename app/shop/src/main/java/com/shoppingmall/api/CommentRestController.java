package com.shoppingmall.api;

import com.shoppingmall.common.BindingResultError;
import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.MessageCode;
import com.shoppingmall.common.ResponseFactory;
import com.shoppingmall.dto.request.CommentRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class CommentRestController {

    private final CommentService commentService;

    /**
     * 댓글 및 대댓글 저장
     *
     * 대댓글 의 경우 parentId를 Front 영역 에서 받아 DB에 등록 하는 형식으로 진행
     * 만약 parentId가 없다면 일반 댓글 등록, 그렇지 않다면 대댓글 등록 으로 간주.
     */
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<CommonResponse> saveComment(
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable("postId") Long postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultError.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(
                    MessageCode.FAIL_SAVE_COMMENT.getCode(),
                    MessageCode.FAIL_SAVE_COMMENT.getMessage(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST
            );
        }

        if (postId == null || postId == 0L) {
            return ResponseFactory.createResponseFactory(
                    MessageCode.NOT_FOUND_POST_ID.getCode(),
                    MessageCode.NOT_FOUND_POST_ID.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        commentRequestDto.setMemberId(1L);
        commentRequestDto.setPostId(postId);

        List<CommentResponseDto> comments = commentService.saveComment(commentRequestDto);
        return ResponseFactory.createResponseFactory(
                MessageCode.SUCCESS_SAVE_COMMENT.getCode(),
                MessageCode.SUCCESS_SAVE_COMMENT.getMessage(),
                comments,
                HttpStatus.OK
        );
    }

    @PutMapping("/post/{postId}/comments")
    public ResponseEntity<CommonResponse> updateCommentByCommentId(
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable("postId") Long postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultError.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(
                    MessageCode.FAIL_UPDATE_COMMENT.getCode(),
                    MessageCode.FAIL_UPDATE_COMMENT.getMessage(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST
            );
        }
        commentRequestDto.setPostId(postId);

        List<CommentResponseDto> comments = commentService.updateCommentByCommentId(commentRequestDto);
        return ResponseFactory.createResponseFactory(
                MessageCode.SUCCESS_UPDATE_COMMENT.getCode(),
                MessageCode.SUCCESS_UPDATE_COMMENT.getMessage(),
                comments,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/post/comments")
    public ResponseEntity<CommonResponse> deleteComments(
            @ModelAttribute CommentRequestDto commentRequestDto) {

        List<CommentResponseDto> comments = commentService.deleteComments(commentRequestDto);

        // SUCCESS로 response 해도 빈 comments 값이 나가기 때문에 상관 없을 것으로 판단
        return ResponseFactory.createResponseFactory(
                MessageCode.SUCCESS_DELETE_COMMENT.getCode(),
                MessageCode.SUCCESS_DELETE_COMMENT.getMessage(),
                comments,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/post/comments/reply")
    public ResponseEntity<CommonResponse> deleteCommentsReply(
            @ModelAttribute CommentRequestDto commentRequestDto) {

        List<CommentResponseDto> comments = commentService.deleteCommentsReply(commentRequestDto);

        return ResponseFactory.createResponseFactory(
                MessageCode.SUCCESS_DELETE_COMMENT.getCode(),
                MessageCode.SUCCESS_DELETE_COMMENT.getMessage(),
                comments,
                HttpStatus.OK
        );
    }
}
