package com.shoppingmall.web;

import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.ProductDetailResponseDto;
import com.shoppingmall.service.ProductService;
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

    @GetMapping(value = {"/", ""})
    public String init(
            SearchRequestDto searchRequestDto,
            Model model) {

        PagingResponseDto<ProductDetailResponseDto> products = productService.getProducts(searchRequestDto);
        log.debug("products = {}", products);

        model.addAttribute("searchRequestDto", searchRequestDto);
        model.addAttribute("products", products);
        return "index";
    }
}
