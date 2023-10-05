package com.multi.posts.controller.api;

import com.multi.common.utils.BindingResultErrorUtils;
import com.multi.common.utils.message.CommonResponse;
import com.multi.common.utils.message.MessageCode;
import com.multi.common.utils.message.ResponseFactory;
import com.multi.posts.dto.request.CommentRequestDto;
import com.multi.posts.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
            @RequestBody @Valid CommentRequestDto commentRequestDto,
            @PathVariable("postId") Long postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultErrorUtils.extractBindingResultErrorMessages(bindingResult);
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

        commentRequestDto.setMemberId(1L); // TODO: replace hard code
        commentRequestDto.setPostId(20L);

        int result = commentService.saveComment(commentRequestDto);
        return ResponseFactory.handlerResponseFactory(result, MessageCode.SUCCESS_SAVE_COMMENT, MessageCode.FAIL_SAVE_COMMENT);
    }

    @PutMapping("/post/comments")
    public ResponseEntity<CommonResponse> updateCommentById(
            @Valid CommentRequestDto commentRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultErrorUtils.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(
                    MessageCode.FAIL_UPDATE_COMMENT.getCode(),
                    MessageCode.FAIL_UPDATE_COMMENT.getMessage(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST
            );
        }

        int result = commentService.updateCommentById(commentRequestDto);
        return ResponseFactory.handlerResponseFactory(result, MessageCode.SUCCESS_UPDATE_COMMENT, MessageCode.FAIL_UPDATE_COMMENT);
    }

    @DeleteMapping("/post/comments")
    public ResponseEntity<CommonResponse> deleteCommentById(CommentRequestDto commentRequestDto) {

        int result = commentService.deleteCommentById(commentRequestDto);
        return ResponseFactory.handlerResponseFactory(result, MessageCode.SUCCESS_DELETE_COMMENT, MessageCode.FAIL_DELETE_COMMENT);
    }
}
