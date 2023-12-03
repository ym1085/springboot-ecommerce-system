package com.shoppingmall.api;

import com.shoppingmall.common.BindingResultError;
import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.MessageCode;
import com.shoppingmall.common.ResponseFactory;
import com.shoppingmall.config.auth.PrincipalDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
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
        return ResponseFactory.createResponseFactory(
                MessageCode.SUCCESS_GET_POSTS.getCode(),
                MessageCode.SUCCESS_GET_POSTS.getMessage(),
                posts,
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("postId") Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ResponseFactory.createResponseFactory(
                MessageCode.SUCCESS_GET_POST.getCode(),
                MessageCode.SUCCESS_GET_POST.getMessage(),
                post,
                HttpStatus.OK
        );
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

        return ResponseFactory.createResponseFactory(MessageCode.SUCCESS_SAVE_POST.getCode(), MessageCode.SUCCESS_SAVE_POST.getMessage(), HttpStatus.OK);
    }

    @PutMapping(value = "/post/{postId}")
    public ResponseEntity<CommonResponse> updatePost(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails, // https://www.baeldung.com/get-user-in-spring-security
            Principal principal,
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

        postRequestDto.setMemberId(1L); // TODO: Spring Security 통해 사용자 정보 받아서 셋팅
        postRequestDto.setPostId(postId);

        MessageCode messageCode = postService.updatePost(postRequestDto);
        return ResponseFactory.createResponseFactory(
            messageCode.getCode(),
            messageCode.getMessage(),
            (messageCode == MessageCode.SUCCESS_UPDATE_POST)
                    ? HttpStatus.OK
                    : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping(value = "/post/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") Long postId) {
        MessageCode messageCode = postService.deletePost(postId);
        return ResponseFactory.createResponseFactory(
                messageCode.getCode(),
                messageCode.getMessage(),
                (messageCode == MessageCode.SUCCESS_DELETE_POST)
                        ? HttpStatus.OK
                        : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
