package com.post.controller.api;

import com.post.constant.StatusEnum;
import com.post.dto.request.CommentRequestDto;
import com.post.dto.resposne.Message;
import com.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    /**
     * 댓글, 대댓글 지정
     *
     * @description 대댓글의 경우는 parentId를 Front 영역에서 받아 DB에 등록하는 형식을 진행
     * 만약 parentId가 없다면 일반 댓글 등록, 그렇지 않다면 대댓글 등록으로 간주 하였습니다.
     */
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity saveComment(@Valid CommentRequestDto commentRequestDto,
                                      @PathVariable("postId") Long postId,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            return new ResponseEntity<>(new Message<>(StatusEnum.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
        }

        if (postId == null) {
            return new ResponseEntity<>(new Message<>(StatusEnum.BAD_REQUEST, StatusEnum.COULD_NOT_FOUND_POST_ID.getMessage()), HttpStatus.BAD_REQUEST);
        }
        commentRequestDto.setMemberId(1L);
        commentRequestDto.setPostId(20L);

        int successId = commentService.saveComment(commentRequestDto);

        ResponseEntity<Message> responseEntity;
        Message message;
        if (successId > 0) {
            message = new Message<>(StatusEnum.OK, StatusEnum.SUCCESS_SAVE_COMMENT.getMessage(), successId);
            responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            message = new Message<>(StatusEnum.INTERNAL_SERVER_ERROR, StatusEnum.COULD_NOT_SAVE_COMMENT.getMessage(), successId);
            responseEntity = new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
