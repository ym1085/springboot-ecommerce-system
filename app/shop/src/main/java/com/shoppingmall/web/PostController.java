package com.shoppingmall.web;

import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.PostResponseDto;
import com.shoppingmall.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping(value = "/post")
    public String getPosts(@ModelAttribute SearchRequestDto searchRequestDto, Model model) {
        PagingResponseDto<PostResponseDto> posts = postService.getPosts(searchRequestDto);
        model.addAttribute("posts", posts);
        model.addAttribute("searchRequestDto", searchRequestDto);
        return "post/list";
    }

    @GetMapping(value = "/post/{postId}")
    public String getPostById(@PathVariable("postId") Long postId, @ModelAttribute SearchRequestDto searchRequestDto, Model model) {
        PostResponseDto post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "post/detail";
    }

    @GetMapping(value = "/post/save")
    public String save() {
        return "post/save";
    }
}
