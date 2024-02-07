package com.shoppingmall.dto.request;

import com.shoppingmall.constant.Gender;
import com.shoppingmall.constant.Role;
import com.shoppingmall.vo.MemberVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSaveRequestDto {

    private Long memberId;

    @NotBlank(message = "이름은 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Size(min = 2, max = 6, message = "두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "ID는 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Size(max = 30, message = "30자 이하의 ID만 입력 가능합니다.")
    private String account;

    @NotBlank(message = "비밀번호는 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$", message = "영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 항목입니다. 다시 시도해주세요.")
    @Email(message = "올바른 메일 형식이 아닙니다. 다시 시도해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다. 다시 시도해주세요.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 휴대폰번호 형식이 아닙니다. 다시 시도해주세요.")
    private String phoneNumber;

    @NotBlank(message = "이메일 인증이 진행되지 않았습니다. 다시 시도해주세요.")
    @Pattern(regexp = "^Y$", message = "사용자 인증에 실패하였습니다. 다시 시도해주세요.")
    private String certYn;

    @NotBlank(message = "아이디 중복 인증이 진행되지 않았습니다. 다시 시도해주세요.")
    @Pattern(regexp = "^Y$", message = "사용자 인증에 실패하였습니다. 다시 시도해주세요.")
    private String accountCertYn;

    @NotBlank(message = "생년월일은 필수 입력 항목입니다. 다시 시도해주세요.")
    private String birthDate;

    private String picture;
    private Gender gender;
    private Role role;

    public MemberVO toEntity() {
        return MemberVO.builder()
                .memberId(memberId)
                .name(name)
                .account(account)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .certYn(certYn)
                .birthDate(birthDate)
                .picture(picture)
                .gender(gender)
                .role(role)
                .build();
    }
}
