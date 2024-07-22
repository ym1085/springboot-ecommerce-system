package com.shoppingmall.api;

import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.config.auth.PrincipalUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shoppingmall.common.code.success.CommonSuccessCode.SUCCESS;
import static com.shoppingmall.common.code.success.post.PostSuccessCode.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class PostRestController {

    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<BaseResponse<?>> getPosts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        return ApiResponseUtils.success(SUCCESS, posts);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<BaseResponse<?>> getPostById(@PathVariable("postId") Integer postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ApiResponseUtils.success(SUCCESS, post);
    }

    @PostMapping("/post")
    public ResponseEntity<BaseResponse<?>> savePost(
            @Valid @ModelAttribute PostSaveRequestDto postSaveRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }
        postSaveRequestDto.setMemberId(1);
        postService.savePost(postSaveRequestDto);
        return ApiResponseUtils.success(SAVE_POST);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<BaseResponse<?>> updatePost(
            @PathVariable("postId") Integer postId,
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @Valid @ModelAttribute PostUpdateRequestDto postUpdateRequestDto,
            BindingResult bindingResult) { // https://www.baeldung.com/get-user-in-spring-security

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }
        postUpdateRequestDto.setMemberId(1);
        postUpdateRequestDto.setPostId(postId);
        postService.updatePost(postUpdateRequestDto);
        return ApiResponseUtils.success(UPDATE_POST);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<BaseResponse<?>> deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
        return ApiResponseUtils.success(DELETE_POST);
    }
}
