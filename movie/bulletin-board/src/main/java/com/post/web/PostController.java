package com.post.web;

import com.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    /**
     * 일반 게시판 전체 게시글 조회
     * @return ResponseEntity 200, 400
     */
    @GetMapping(value = "/post")
    public ResponseEntity getPosts() {
        return new ResponseEntity<>(postService.getPosts(), HttpStatus.OK);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity getPostById(@PathVariable("id") Long id) {
        return new ResponseEntity(postService.getPostById(id), HttpStatus.OK);
    }

}
