package com.shoppingmall.api;

import com.shoppingmall.common.BindingResultError;
import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.MessageCode;
import com.shoppingmall.common.ResponseFactory;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.request.PostRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import com.shoppingmall.service.FileService;
import com.shoppingmall.service.PostService;
import com.shoppingmall.utils.FileHandlerHelper;
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
public class PostRestController {

    private final PostService postService;
    private final FileHandlerHelper fileHandlerHelper;
    private final FileService fileService;

    @GetMapping(value = "/post")
    public ResponseEntity<CommonResponse> getPosts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_GET_POSTS.getCode(), MessageCode.SUCCESS_GET_POSTS.getMessage(), posts, HttpStatus.OK);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("id") Long id) {
        PostResponseDto post = postService.getPostById(id);
        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_GET_POST.getCode(), MessageCode.SUCCESS_GET_POST.getMessage(), post, HttpStatus.OK);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<CommonResponse> savePost(
            @ModelAttribute @Valid PostRequestDto postRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultError.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(
                    MessageCode.FAIL_SAVE_POST.getCode(),
                    MessageCode.FAIL_SAVE_POST.getMessage(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST
            );
        }

        postRequestDto.setMemberId(1L);
        Long postId = postService.savePost(postRequestDto);

        List<FileRequestDto> fileRequestDtos = fileHandlerHelper.uploadFiles(postRequestDto.getFiles(), postRequestDto.getFileType());
        fileService.saveFiles(postId, fileRequestDtos);

        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_SAVE_POST.getCode(), MessageCode.SUCCESS_SAVE_POST.getMessage(), HttpStatus.OK);
    }

    @PutMapping(value = "/post/{id}")
    public ResponseEntity<CommonResponse> updatePost(
            @PathVariable("id") Long id,
            @Valid PostRequestDto postRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessage = BindingResultError.extractBindingResultErrorMessages(bindingResult);
            return ResponseFactory.createResponseFactory(
                    MessageCode.FAIL_UPDATE_POST.getCode(),
                    MessageCode.FAIL_UPDATE_POST.getMessage(),
                    errorMessage,
                    HttpStatus.BAD_REQUEST
            );
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
