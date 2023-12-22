package com.shoppingmall.api;

import com.shoppingmall.common.*;
import com.shoppingmall.dto.request.PostRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.PostService;
import com.shoppingmall.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class PostRestController {

    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<CommonResponse> getPosts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        return ApiUtils.success(
                SuccessCode.SUCCESS_GET_POSTS.getCode(),
                SuccessCode.SUCCESS_GET_POSTS.getMessage(),
                posts,
                HttpStatus.OK
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("postId") Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ApiUtils.success(
                SuccessCode.SUCCESS_GET_POST.getCode(),
                SuccessCode.SUCCESS_GET_POST.getMessage(),
                post,
                HttpStatus.OK
        );
    }

    @PostMapping("/post")
    public ResponseEntity<CommonResponse> savePost(
            @Valid @ModelAttribute PostRequestDto postRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        postRequestDto.setMemberId(1L);
        Long postId = postService.savePost(postRequestDto);
        boolean success = postId > 0; // save 후 postId를 받기에 해당 케이스만 함수 호출 없이 별도 처리
        MessageCode messageCode = success ? SuccessCode.SUCCESS_SAVE_POST : ErrorCode.FAIL_SAVE_POST;

        return ApiUtils.success(
                messageCode.getCode(),
                messageCode.getMessage(),
                success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> updatePost(
            @PathVariable("postId") Long postId,
            @Valid @ModelAttribute PostRequestDto postRequestDto,
            BindingResult bindingResult,
            //@AuthenticationPrincipal PrincipalDetails principalDetails, // https://www.baeldung.com/get-user-in-spring-security
            Principal principal) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        postRequestDto.setMemberId(1L);
        postRequestDto.setPostId(postId);

        int responseCode = postService.updatePost(postRequestDto);
        boolean success = ResponseUtils.isSuccessResponseCode(responseCode);
        MessageCode messageCode = success ? SuccessCode.SUCCESS_UPDATE_POST : ErrorCode.FAIL_UPDATE_POST;

        return ApiUtils.success(
                messageCode.getCode(),
                messageCode.getMessage(),
                success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") Long postId) {
        int responseCode = postService.deletePost(postId);
        boolean success = ResponseUtils.isSuccessResponseCode(responseCode);
        MessageCode messageCode = success ? SuccessCode.SUCCESS_DELETE_POST : ErrorCode.FAIL_DELETE_POST;

        return ApiUtils.success(
                messageCode.getCode(),
                messageCode.getMessage(),
                success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
