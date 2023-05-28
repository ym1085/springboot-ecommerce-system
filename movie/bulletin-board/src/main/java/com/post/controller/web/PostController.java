package com.post.controller.web;

import com.post.constant.ResponseCode;
import com.post.constant.StatusEnum;
import com.post.dto.request.FileRequestDto;
import com.post.dto.request.PostRequestDto;
import com.post.dto.request.SearchRequestDto;
import com.post.dto.resposne.CommentResponseDto;
import com.post.dto.resposne.Message;
import com.post.dto.resposne.PagingResponseDto;
import com.post.dto.resposne.PostResponseDto;
import com.post.service.CommentService;
import com.post.service.FileService;
import com.post.service.PostService;
import com.post.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 28
 * @description :   현재 화면은 따로 만들지 않았습니다. 추후 협업자가 화면을 그리기 위해 사용이 되는 컨트롤러 입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final FileUtils fileUtils;
    private final FileService fileService;
    private final CommentService commentService;

    @GetMapping(value = "/post")
    public String getPosts(SearchRequestDto searchRequestDto, Model model) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping(value = "/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        PostResponseDto post = postService.getPostById(id);
        List<CommentResponseDto> comments = commentService.getComments(id);

        model.addAttribute("post", post);
        model.addAttribute("comment", comments);
        return "post/detail";
    }

    @PostMapping(value = "/post")
    public @ResponseBody ResponseEntity savePost(@Valid PostRequestDto postRequestDto,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                sb.append(error.getDefaultMessage());
            }
            Message message = new Message(StatusEnum.BAD_REQUEST, sb.toString());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        postRequestDto.setMemberId(1L); // Todo: Test용 member_id security 되면 지워주세요
        Long postId = postService.savePost(postRequestDto);

        List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(postRequestDto.getFiles());
        fileService.saveFiles(postId, fileRequestDtos);

        return new ResponseEntity<>(ResponseCode.SUCCESS.getResponseCode(), HttpStatus.OK);
    }

    @PutMapping(value = "/post/{id}")
    public @ResponseBody ResponseEntity updatePost(@PathVariable("id") Long id,
                                     @Valid PostRequestDto postRequestDto,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Message message = new Message(StatusEnum.BAD_REQUEST, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // Todo: 현재 로그인 한 Principal 유저 정보와 질의를 통해 얻은 User 정보를 비교 후 수정 여부 판단,
        // 회원 쪽 기능이 구현이 되지 않아 일단 보류

        postRequestDto.setMemberId(1L); // Todo: Test용 member_id security 되면 지워주세요
        postRequestDto.setPostId(id);

        return new ResponseEntity<>(new Message<>(StatusEnum.OK, ResponseCode.SUCCESS.getResponseCode(), postService.updatePost(postRequestDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/post/{id}")
    public @ResponseBody ResponseEntity deletePost(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new Message<>(StatusEnum.OK, ResponseCode.SUCCESS.getResponseCode(), postService.deletePost(id)), HttpStatus.OK);
    }
}
