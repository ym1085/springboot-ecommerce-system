package com.shoppingmall.api;

import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.dto.request.CommentDeleteRequestDto;
import com.shoppingmall.dto.request.CommentSaveRequestDto;
import com.shoppingmall.dto.request.CommentUpdateRequestDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.CommentService;
import com.shoppingmall.vo.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.shoppingmall.common.code.success.post.PostSuccessCode.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<BaseResponse<?>> saveComment(
            @Valid @RequestBody CommentSaveRequestDto commentSaveRequestDto,
            @PathVariable("postId") Integer postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }
        commentSaveRequestDto.setMemberId(1);
        commentSaveRequestDto.setPostId(postId);

        List<Comment> comments = commentService.saveComment(commentSaveRequestDto);
        return ApiResponseUtils.success(SUCCESS_SAVE_COMMENT, comments);
    }

    @PutMapping("/post/{postId}/comments")
    public ResponseEntity<BaseResponse<?>> updateCommentByCommentId(
            @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto,
            @PathVariable("postId") Integer postId,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }
        commentUpdateRequestDto.setPostId(postId);
        List<Comment> comments = commentService.updateCommentByCommentId(commentUpdateRequestDto);
        return ApiResponseUtils.success(SUCCESS_UPDATE_COMMENT, comments);
    }

    @DeleteMapping("/post/comments")
    public ResponseEntity<BaseResponse<?>> deleteComments(
            @ModelAttribute CommentDeleteRequestDto commentDeleteRequestDto) {
        List<Comment> comments = commentService.deleteComments(commentDeleteRequestDto);
        return ApiResponseUtils.success(SUCCESS_DELETE_COMMENT, comments);
    }

    @DeleteMapping("/post/comments/reply")
    public ResponseEntity<BaseResponse<?>> deleteCommentsReply(
            @ModelAttribute CommentDeleteRequestDto commentDeleteRequestDto) {
        List<Comment> comments = commentService.deleteCommentsReply(commentDeleteRequestDto);
        return ApiResponseUtils.success(SUCCESS_DELETE_COMMENT, comments);
    }
}
