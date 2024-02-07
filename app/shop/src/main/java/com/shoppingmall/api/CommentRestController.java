package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.CommentDeleteRequestDto;
import com.shoppingmall.dto.request.CommentSaveRequestDto;
import com.shoppingmall.dto.request.CommentUpdateRequestDto;
import com.shoppingmall.dto.response.CommentResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<CommonResponse> saveComment(
            @Valid @RequestBody CommentSaveRequestDto commentSaveRequestDto,
            @PathVariable("postId") Long postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        commentSaveRequestDto.setMemberId(1L);
        commentSaveRequestDto.setPostId(postId);

        List<CommentResponseDto> comments = commentService.saveComment(commentSaveRequestDto);
        return ApiUtils.success(
                SuccessCode.SUCCESS_SAVE_COMMENT.getHttpStatus(),
                SuccessCode.SUCCESS_SAVE_COMMENT.getMessage(),
                comments
        );
    }

    @PutMapping("/post/{postId}/comments")
    public ResponseEntity<CommonResponse> updateCommentByCommentId(
            @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto,
            @PathVariable("postId") Long postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        commentUpdateRequestDto.setPostId(postId);

        List<CommentResponseDto> comments = commentService.updateCommentByCommentId(commentUpdateRequestDto);
        return ApiUtils.success(
                SuccessCode.SUCCESS_UPDATE_COMMENT.getHttpStatus(),
                SuccessCode.SUCCESS_UPDATE_COMMENT.getMessage(),
                comments
        );
    }

    @DeleteMapping("/post/comments")
    public ResponseEntity<CommonResponse> deleteComments(
            @ModelAttribute CommentDeleteRequestDto commentDeleteRequestDto) {

        List<CommentResponseDto> comments = commentService.deleteComments(commentDeleteRequestDto);

        return ApiUtils.success(
                SuccessCode.SUCCESS_DELETE_COMMENT.getHttpStatus(),
                SuccessCode.SUCCESS_DELETE_COMMENT.getMessage(),
                comments
        );
    }

    @DeleteMapping("/post/comments/reply")
    public ResponseEntity<CommonResponse> deleteCommentsReply(
            @ModelAttribute CommentDeleteRequestDto commentDeleteRequestDto) {

        List<CommentResponseDto> comments = commentService.deleteCommentsReply(commentDeleteRequestDto);

        return ApiUtils.success(
                SuccessCode.SUCCESS_DELETE_COMMENT.getHttpStatus(),
                SuccessCode.SUCCESS_DELETE_COMMENT.getMessage(),
                comments);
    }
}
