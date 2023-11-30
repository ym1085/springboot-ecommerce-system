package com.shoppingmall.api;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.ProductRequestDto;
import com.shoppingmall.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShopApplication.class)
@Transactional
@ActiveProfiles("local")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindAllProductsWithFiles_thenCorrect() {

        //given
//        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
//                .name(null)
//                .account("test01")
//                .password("yyyyjjj*1")
//                .email("test@gmail.com")
//                .phoneNumber("010-2222-3333")
//                .certYn("Y")
//                .accountCertYn("Y")
//                .gender(Gender.M)
//                .birthDate("1993-08-23")
//                .build();
//
//        //when
//        ResultActions result  = mockMvc.perform(
//                post("/api/v1/member/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(memberRequestDto))
//        );

        //then
//        result.andExpect(status().isBadRequest())
//                .andDo(print())
//                .andExpect(jsonPath("$.code", equalTo(2006)))
//                .andExpect(jsonPath("$.message", Matchers.containsString("회원 가입에 실패하였습니다")))
//                .andExpect(jsonPath("$.errorMessage.name", Matchers.containsString("이름은 필수 입력 항목 입니다")));


        List<ProductRequestDto> result = productRepository.findAllProductsWithFiles();
        assertThat(result).hasSize(20);
    }
}
