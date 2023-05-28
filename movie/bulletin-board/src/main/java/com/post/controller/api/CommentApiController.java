package com.post.controller.api;

import com.post.constant.ResponseCode;
import com.post.constant.StatusEnum;
import com.post.dto.request.CommentRequestDto;
import com.post.dto.resposne.Message;
import com.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity saveComment(@Valid CommentRequestDto commentRequestDto,
                                      @PathVariable("postId") Long postId,
                                      BindingResult bindingResult) {

        Message message = null;
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
               sb.append(error.getDefaultMessage());
            }
            message = new Message(StatusEnum.BAD_REQUEST, sb.toString());
            return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
        }

        if (postId == null) {
            message = new Message<>(StatusEnum.BAD_REQUEST, "게시판 번호가 존재하지 않습니다.");
            return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
        }

        commentRequestDto.setMemberId(1L); // Todo: security 적용 후 값 받아서 셋팅
        commentRequestDto.setPostId(20L); // 난수를 넣어줄까 그냥...?

        int successId = commentService.saveComment(commentRequestDto);

        message = new Message<>(StatusEnum.INTERNAL_SERVER_ERROR, successId);;
        if (successId > 0) {
            new Message<>(StatusEnum.OK, ResponseCode.SUCCESS, successId);
        }

        // Todo: Response에 data null로 나가는데 수정 필요
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
