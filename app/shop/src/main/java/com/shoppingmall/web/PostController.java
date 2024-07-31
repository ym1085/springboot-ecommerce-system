package com.shoppingmall.web;

import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.service.PostService;
import com.shoppingmall.vo.PagingResponse;
import com.shoppingmall.vo.Post;
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

    @GetMapping("/post")
    public String getPosts(@ModelAttribute SearchRequestDto searchRequestDto, Model model) {
        PagingResponse<Post> posts = postService.getPosts(searchRequestDto);
        model.addAttribute("posts", posts);
        model.addAttribute("searchRequestDto", searchRequestDto);
        return "post/list";
    }

    @GetMapping("/post/{postId}")
    public String getPostById(@PathVariable("postId") Integer postId, @ModelAttribute SearchRequestDto searchRequestDto, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "post/detail";
    }

    @GetMapping("/post/save")
    public String save() {
        return "post/save";
    }
}
