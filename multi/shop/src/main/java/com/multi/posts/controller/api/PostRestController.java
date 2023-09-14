package com.multi.posts.controller.api;

import com.multi.common.utils.ErrorUtils;
import com.multi.common.utils.FileUtils;
import com.multi.common.utils.message.CommonResponse;
import com.multi.common.utils.message.MessageCode;
import com.multi.common.utils.message.ResponseFactory;
import com.multi.posts.dto.request.FileRequestDto;
import com.multi.posts.dto.request.PostRequestDto;
import com.multi.posts.dto.request.SearchRequestDto;
import com.multi.posts.dto.resposne.CommentResponseDto;
import com.multi.posts.dto.resposne.PagingResponseDto;
import com.multi.posts.dto.resposne.PostResponseDto;
import com.multi.posts.service.CommentService;
import com.multi.posts.service.FileService;
import com.multi.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class PostRestController {

    private final PostService postService;
    private final FileUtils fileUtils;
    private final FileService fileService;
    private final CommentService commentService;

    @GetMapping(value = "/post")
    public ResponseEntity<CommonResponse> getPosts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_GET_POSTS, posts, HttpStatus.OK);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("id") Long id) {
        PostResponseDto post = postService.getPostById(id);
        List<CommentResponseDto> comments = commentService.getComments(id);

        post.addComments(comments);

        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_GET_POST, post, HttpStatus.OK);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<CommonResponse> savePost(
            @RequestBody @Valid PostRequestDto postRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = ErrorUtils.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(MessageCode.FAIL_SAVE_POST, errorMessage, HttpStatus.BAD_REQUEST);
        }

        postRequestDto.setMemberId(1L); // TODO: replace hard code
        Long postId = postService.savePost(postRequestDto);

        List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(postRequestDto.getFiles());
        fileService.saveFiles(postId, fileRequestDtos);

        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_SAVE_POST, MessageCode.SUCCESS_SAVE_POST.getMessage(), HttpStatus.OK);
    }

    @PutMapping(value = "/post/{id}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("id") Long id,
                                     @Valid PostRequestDto postRequestDto,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = ErrorUtils.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(MessageCode.FAIL_UPDATE_POST, errorMessage, HttpStatus.BAD_REQUEST);
        }

        postRequestDto.setMemberId(1L); // TODO: replace hard code
        postRequestDto.setPostId(id);

        int result = postService.updatePost(postRequestDto);
        return ResponseFactory.handlerResponseFactory(result, MessageCode.SUCCESS_UPDATE_POST, MessageCode.FAIL_UPDATE_POST);
    }

    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("id") Long id) {
        int result = postService.deletePost(id);
        return ResponseFactory.handlerResponseFactory(result, MessageCode.SUCCESS_DELETE_POST, MessageCode.FAIL_DELETE_POST);
    }
}