package com.multi.posts.controller.web;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 28
 * @description :   현재 화면은 따로 만들지 않았습니다. 추후 협업자가 화면을 그리기 위해 사용이 되는 컨트롤러 입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final FileUtils fileUtils;
    private final FileService fileService;
    private final CommentService commentService;

    @GetMapping(value = "/post")
    public String getPosts(SearchRequestDto searchRequestDto, Model model) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping(value = "/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        PostResponseDto post = postService.getPostById(id);
        List<CommentResponseDto> comments = commentService.getComments(id);

        model.addAttribute("post", post);
        model.addAttribute("comment", comments);
        return "post/detail";
    }

    @PostMapping(value = "/post")
    public @ResponseBody ResponseEntity<?> savePost(@Valid PostRequestDto postRequestDto,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .reduce("", (acc, message) -> acc + message);

            // Fixme: 아직 화면이 없어서 일단은... 추후 수정 하자
            ApiResponse<String> message = new ApiResponse<>(StatusEnum.BAD_REQUEST, errorMessage);
            return ResponseEntity.badRequest().body(message);
        }

        postRequestDto.setMemberId(1L);
        Long postId = postService.savePost(postRequestDto);

        List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(postRequestDto.getFiles());
        fileService.saveFiles(postId, fileRequestDtos);

        ApiResponse<Integer> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_SAVE_POST.getMessage(), ResponseCode.SUCCESS.getResponseCode());
        return ResponseEntity.ok().body(message);
    }

    @PutMapping(value = "/post/{id}")
    public @ResponseBody ResponseEntity<?> updatePost(@PathVariable("id") Long id,
                                     @Valid PostRequestDto postRequestDto,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            ApiResponse<String> message = new ApiResponse<>(StatusEnum.BAD_REQUEST, errorMessage);
            return ResponseEntity.badRequest().body(message);
        }

        postRequestDto.setMemberId(1L);
        postRequestDto.setPostId(id);

        int updatedCount = postService.updatePost(postRequestDto);
        ApiResponse<Integer> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_UPDATE_POST.getMessage(), updatedCount);
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping(value = "/post/{id}")
    public @ResponseBody ResponseEntity<ApiResponse<Integer>> deletePost(@PathVariable("id") Long id) {
        int deletedCount = postService.deletePost(id);
        ApiResponse<Integer> message = new ApiResponse<>(StatusEnum.OK, StatusEnum.SUCCESS_DELETE_POST.getMessage(), deletedCount);
        return ResponseEntity.ok().body(message);
    }
}
