package com.post.web;

import com.post.constant.ErrorMessage;
import com.post.constant.ResponseCode;
import com.post.service.FileService;
import com.post.service.PostService;
import com.post.utils.FileUtils;
import com.post.web.dto.request.FileRequestDto;
import com.post.web.dto.request.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final FileUtils fileUtils;
    private final FileService fileService;

    @GetMapping(value = "/post")
    public ResponseEntity getPosts() {
        return new ResponseEntity<>(postService.getPosts(), HttpStatus.OK);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity getPostById(@PathVariable("id") Long id) {
        return new ResponseEntity(postService.getPostById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/post")
    public ResponseEntity savePost(@Valid @ModelAttribute PostRequestDto postRequestDto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorMessage.INVALID_REQUEST_PARAMETER.getMessage(), HttpStatus.BAD_REQUEST);
        }
        postRequestDto.setMemberId(1L); // Todo: Test용 member_id security 되면 지워주세요
        Long postId = postService.savePost(postRequestDto);

        List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(postRequestDto.getFiles());
        int i = fileService.saveFiles(postId, fileRequestDtos);

        return new ResponseEntity<>(ResponseCode.SUCCESS.getResponseCode(), HttpStatus.OK);
    }

    @PutMapping(value = "/post/{id}")
    public ResponseEntity uploadPost(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute PostRequestDto postRequestDto,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        // Todo: 현재 로그인 한 Principal 유저 정보와 질의를 통해 얻은 User 정보를 비교 후 수정 여부 판단,
        // 회원 쪽 기능이 구현이 되지 않아 일단 보류

        postRequestDto.setMemberId(1L); // Todo: Test용 member_id security 되면 지워주세요
        postRequestDto.setPostId(id);

        return new ResponseEntity<>(postService.uploadPost(postRequestDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Long id) {
        return new ResponseEntity<>(postService.deletePost(id), HttpStatus.OK);
    }
}
