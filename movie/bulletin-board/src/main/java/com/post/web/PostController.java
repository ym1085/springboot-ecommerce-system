package com.post.web;

import com.post.constant.ErrorMessage;
import com.post.service.FileService;
import com.post.service.PostService;
import com.post.utils.FileUtils;
import com.post.web.dto.request.FileSaveRequestDto;
import com.post.web.dto.request.PostRequestSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity savePost(@Valid @ModelAttribute PostRequestSaveDto postRequestSaveDto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorMessage.INVALID_ARGUMENTS_REQUEST.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Long postId = postService.savePost(postRequestSaveDto);

        List<FileSaveRequestDto> fileSaveRequestDto = fileUtils.uploadFiles(postRequestSaveDto.getFiles());
        fileService.saveFiles(postId, fileSaveRequestDto);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
