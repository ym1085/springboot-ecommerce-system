package com.shoppingmall.api;

import com.shoppingmall.common.ApiUtils;
import com.shoppingmall.common.CommonResponse;
import com.shoppingmall.common.SuccessCode;
import com.shoppingmall.dto.request.PostRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.PostService;
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

    @GetMapping(value = "/post")
    public ResponseEntity<CommonResponse> getPosts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        return ApiUtils.success(
                SuccessCode.SUCCESS_GET_POSTS.getCode(),
                SuccessCode.SUCCESS_GET_POSTS.getMessage(),
                posts,
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("postId") Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ApiUtils.success(
                SuccessCode.SUCCESS_GET_POST.getCode(),
                SuccessCode.SUCCESS_GET_POST.getMessage(),
                post,
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/post")
    public ResponseEntity<CommonResponse> savePost(
            @Valid @ModelAttribute PostRequestDto postRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        postRequestDto.setMemberId(1L);
        Long postId = postService.savePost(postRequestDto);

        return ApiUtils.success(
                SuccessCode.SUCCESS_SAVE_POST.getCode(),
                SuccessCode.SUCCESS_SAVE_POST.getMessage(),
                HttpStatus.OK
        );
    }

    @PutMapping(value = "/post/{postId}")
    public ResponseEntity<CommonResponse> updatePost(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody PostRequestDto postRequestDto,
            BindingResult bindingResult,
            //@AuthenticationPrincipal PrincipalDetails principalDetails, // https://www.baeldung.com/get-user-in-spring-security
            Principal principal) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        postRequestDto.setMemberId(1L);
        postRequestDto.setPostId(postId);

        int responseCode = postService.updatePost(postRequestDto);
        return ApiUtils.success(
                SuccessCode.SUCCESS_UPDATE_POST.getCode(),
                SuccessCode.SUCCESS_UPDATE_POST.getMessage(),
                responseCode == 1 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping(value = "/post/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") Long postId) {
        int responseCode = postService.deletePost(postId);
        return ApiUtils.success(
                SuccessCode.SUCCESS_DELETE_POST.getCode(),
                SuccessCode.SUCCESS_DELETE_POST.getMessage(),
                responseCode == 1 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
