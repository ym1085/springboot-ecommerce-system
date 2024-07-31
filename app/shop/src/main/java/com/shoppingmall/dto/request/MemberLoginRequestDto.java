package com.shoppingmall.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberLoginRequestDto {

    @NotBlank(message = "ID는 필수 입력  항목 입니다. 다시 시도해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$", message = "영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요.")
    private String password;
}
