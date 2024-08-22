package com.shoppingmall.web;

import com.shoppingmall.config.auth.attribute.SessionMember;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.service.ProductService;
import com.shoppingmall.vo.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProductService productService;
    private final HttpSession session;

    @GetMapping(value = {"/", ""})
    public String init(SearchRequestDto searchRequestDto, Model model) {
        SessionMember loginSessionUser = (SessionMember) session.getAttribute("LOGIN_SESSION_USER");
        if (loginSessionUser != null) {
            model.addAttribute("name", loginSessionUser.getName());
            model.addAttribute("email", loginSessionUser.getEmail());
        }
        ProductResponse products = productService.getProducts(searchRequestDto);
        model.addAttribute("searchRequestDto", searchRequestDto);
        model.addAttribute("products", products);
        return "index";
    }
}
