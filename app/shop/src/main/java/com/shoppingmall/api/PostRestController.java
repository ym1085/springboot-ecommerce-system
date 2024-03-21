package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.response.SuccessCode;
import com.shoppingmall.dto.request.PostSaveRequestDto;
import com.shoppingmall.dto.request.PostUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                SuccessCode.OK.getCode(),
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage(),
                posts
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("postId") Integer postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ApiUtils.success(
                SuccessCode.OK.getCode(),
                SuccessCode.OK.getHttpStatus(),
                SuccessCode.OK.getMessage(),
                post
        );
    }

    @PostMapping("/post")
    public ResponseEntity<CommonResponse> savePost(
            @Valid @ModelAttribute PostSaveRequestDto postSaveRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        postSaveRequestDto.setMemberId(1);
        postService.savePost(postSaveRequestDto);

        return ApiUtils.success(
                SuccessCode.SAVE_POST.getCode(),
                SuccessCode.SAVE_POST.getHttpStatus(),
                SuccessCode.SAVE_POST.getMessage()
        );
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> updatePost(
            @PathVariable("postId") Integer postId,
            @Valid @ModelAttribute PostUpdateRequestDto postUpdateRequestDto,
            BindingResult bindingResult,
            //@AuthenticationPrincipal PrincipalDetails principalDetails, // https://www.baeldung.com/get-user-in-spring-security
            Principal principal) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        postUpdateRequestDto.setMemberId(1);
        postUpdateRequestDto.setPostId(postId);

        postService.updatePost(postUpdateRequestDto);

        return ApiUtils.success(
                SuccessCode.UPDATE_POST.getCode(),
                SuccessCode.UPDATE_POST.getHttpStatus(),
                SuccessCode.UPDATE_POST.getMessage()
        );
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);

        return ApiUtils.success(
                SuccessCode.DELETE_POST.getCode(),
                SuccessCode.DELETE_POST.getHttpStatus(),
                SuccessCode.DELETE_POST.getMessage()
        );
    }
}
