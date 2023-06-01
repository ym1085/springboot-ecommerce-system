package com.multi.posts.controller.api;

import com.multi.posts.constant.ResponseCode;
import com.multi.posts.constant.StatusEnum;
import com.multi.posts.dto.request.FileRequestDto;
import com.multi.posts.dto.request.PostRequestDto;
import com.multi.posts.dto.request.SearchRequestDto;
import com.multi.posts.dto.resposne.CommentResponseDto;
import com.multi.posts.dto.resposne.PagingResponseDto;
import com.multi.posts.dto.resposne.PostResponseDto;
import com.multi.posts.service.CommentService;
import com.multi.posts.service.FileService;
import com.multi.posts.service.PostService;
import com.multi.utils.ApiResponse;
import com.multi.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Fixme: ResponseEntity 목적에 맞게 반환 하도록 수정 필요
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class PostApiController {

    private final PostService postService;
    private final FileUtils fileUtils;
    private final FileService fileService;
    private final CommentService commentService;

    @GetMapping(value = "/post")
    public ResponseEntity<ApiResponse<PagingResponseDto<PostResponseDto>>> getPosts(SearchRequestDto searchRequestDto) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        ApiResponse<PagingResponseDto<PostResponseDto>> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_GET_POSTS.getMessage(), posts);
        return ResponseEntity.ok(message);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPostById(@PathVariable("id") Long id) {
        PostResponseDto post = postService.getPostById(id);
        List<CommentResponseDto> comments = commentService.getComments(id);
        post.addComments(comments);

        StatusEnum status = StringUtils.isNotBlank(post.getTitle()) ? StatusEnum.OK : StatusEnum.INTERNAL_SERVER_ERROR;
        ApiResponse<PostResponseDto> message = new ApiResponse<>(status, StatusEnum.SUCCESS_GET_POST.getMessage(), post);
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<?> savePost(@Valid PostRequestDto postRequestDto,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .reduce("", (acc, message) -> acc + message);

            ApiResponse<String> message = new ApiResponse<>(StatusEnum.BAD_REQUEST, errorMessage);
            return ResponseEntity.badRequest().body(message);
        }

        postRequestDto.setMemberId(1L);
        Long postId = postService.savePost(postRequestDto);

        List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(postRequestDto.getFiles());
        fileService.saveFiles(postId, fileRequestDtos);

        ApiResponse<Integer> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_SAVE_COMMENT.getMessage(), ResponseCode.SUCCESS.getResponseCode());
        return ResponseEntity.ok(message);
    }

    @PutMapping(value = "/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") Long id,
                                     @Valid PostRequestDto postRequestDto,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ApiResponse<String> message = new ApiResponse<>(StatusEnum.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        postRequestDto.setMemberId(1L);
        postRequestDto.setPostId(id);

        int updatedCount = postService.updatePost(postRequestDto);
        ApiResponse<Integer> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_UPDATE_POST.getMessage(), updatedCount);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<ApiResponse<Integer>> deletePost(@PathVariable("id") Long id) {
        int deletedCount = postService.deletePost(id);
        ApiResponse<Integer> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_DELETE_POST.getMessage(), deletedCount);
        return ResponseEntity.ok(message);
    }
}
