package com.shoppingmall.web;

import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.service.ProductService;
import com.shoppingmall.vo.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProductService productService;

    /**
     * 메인 페이지 접근
     */
    @GetMapping(value = {"/", ""})
    public String init(SearchRequestDto searchRequestDto, Model model) {
        ProductResponse products = productService.getProducts(searchRequestDto);
        model.addAttribute("searchRequestDto", searchRequestDto);
        model.addAttribute("products", products);
        return "index";
    }
}
