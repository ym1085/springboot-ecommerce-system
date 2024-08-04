package com.shoppingmall.api;

import com.shoppingmall.common.dto.BaseResponse;
import com.shoppingmall.common.utils.ApiResponseUtils;
import com.shoppingmall.config.auth.PrincipalUserDetails;
import com.shoppingmall.dto.request.PostSaveRequestDto;
import com.shoppingmall.dto.request.PostUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.exception.MemberException;
import com.shoppingmall.utils.SecurityUtils;
import com.shoppingmall.vo.Member;
import com.shoppingmall.vo.PagingResponse;
import com.shoppingmall.exception.InvalidParameterException;
import com.shoppingmall.service.PostService;
import com.shoppingmall.vo.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shoppingmall.common.code.failure.member.MemberFailureCode.NOT_LOGIN_MEMBER;
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
        PagingResponse<Post> posts = postService.getPosts(searchRequestDto);
        return ApiResponseUtils.success(SUCCESS, posts);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<BaseResponse<?>> getPostById(@PathVariable("postId") Integer postId) {
        Post post = postService.getPostById(postId);
        return ApiResponseUtils.success(SUCCESS, post);
    }

    @PostMapping("/post")
    public ResponseEntity<BaseResponse<?>> savePost(
            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
            @Valid @ModelAttribute PostSaveRequestDto postSaveRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidParameterException(bindingResult);
        }

        if (!SecurityUtils.isValidLoginMember(principalUserDetails)) {
            log.error(NOT_LOGIN_MEMBER.getMessage());
            throw new MemberException(NOT_LOGIN_MEMBER);
        }
        Member member = principalUserDetails.getLoginMember();
        postSaveRequestDto.setMemberId(member.getMemberId());
        postService.savePost(postSaveRequestDto);
        return ApiResponseUtils.success(SUCCESS_SAVE_POST);
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

        if (!SecurityUtils.isValidLoginMember(principalUserDetails)) {
            log.error(NOT_LOGIN_MEMBER.getMessage());
            throw new MemberException(NOT_LOGIN_MEMBER);
        }
        Member member = principalUserDetails.getLoginMember();
        postUpdateRequestDto.setMemberId(member.getMemberId());
        postUpdateRequestDto.setPostId(postId);
        postService.updatePost(postUpdateRequestDto);
        return ApiResponseUtils.success(SUCCESS_UPDATE_POST);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<BaseResponse<?>> deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
        return ApiResponseUtils.success(SUCCESS_DELETE_POST);
    }
}
