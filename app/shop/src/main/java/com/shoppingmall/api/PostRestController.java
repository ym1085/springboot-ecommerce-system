package com.shoppingmall.api;

import com.shoppingmall.common.response.ApiUtils;
import com.shoppingmall.common.response.CommonResponse;
import com.shoppingmall.common.success.CommonSuccessCode;
import com.shoppingmall.common.success.PostSuccessCode;
import com.shoppingmall.dto.request.PostRequestDto;
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
                CommonSuccessCode.SUCCESS_CODE.getHttpStatus(),
                CommonSuccessCode.SUCCESS_CODE.getMessage(),
                posts
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> getPostById(@PathVariable("postId") Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ApiUtils.success(
                CommonSuccessCode.SUCCESS_CODE.getHttpStatus(),
                CommonSuccessCode.SUCCESS_CODE.getMessage(),
                post
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

        return ApiUtils.success(
                PostSuccessCode.SUCCESS_SAVE_POST.getHttpStatus(),
                PostSuccessCode.SUCCESS_SAVE_POST.getMessage()
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

        return ApiUtils.success(
                PostSuccessCode.SUCCESS_UPDATE_POST.getHttpStatus(),
                PostSuccessCode.SUCCESS_UPDATE_POST.getMessage()
        );
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") Long postId) {
        int responseCode = postService.deletePost(postId);

        return ApiUtils.success(
                PostSuccessCode.SUCCESS_DELETE_POST.getHttpStatus(),
                PostSuccessCode.SUCCESS_DELETE_POST.getMessage()
        );
    }
}
