package com.multi.posts.controller.web;

import com.multi.posts.dto.request.SearchRequestDto;
import com.multi.posts.dto.resposne.CommentResponseDto;
import com.multi.posts.dto.resposne.PagingResponseDto;
import com.multi.posts.dto.resposne.PostResponseDto;
import com.multi.posts.service.CommentService;
import com.multi.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
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
}
