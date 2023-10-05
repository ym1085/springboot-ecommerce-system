package com.multi.member.dto.request;

import com.multi.member.constant.Gender;
import com.multi.member.constant.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class MemberRequestDto {
    private Long memberId;

    @NotBlank(message = "이름은 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Size(min = 2, max = 6, message = "두 글자 이상, 여섯 글자 이하의 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "ID는 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Size(max = 30, message = "30자 이하의 ID만 입력 가능합니다.")
    private String account;

    @NotBlank(message = "비밀번호는 필수 입력 항목 입니다. 다시 시도해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$",
            message = "영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요.") // 검증 : https://regexr.com/
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

    private String picture;
    private Gender gender;
    private Role role;

    @NotBlank(message = "생년월일은 필수 입력 항목입니다. 다시 시도해주세요.")
    private String birthDate;

    // 사용자가 회원 가입 요청 시, 해당 사용자의 비밀번호를 해싱(hashing), 변환 하기 위함
    public void encodeMemberPassword(PasswordEncoder passwordEncoder) {
        if (StringUtils.isEmpty(this.password)) {
            return;
        }
        this.password = passwordEncoder.encode(this.password);
    }

    public void replaceHyphen() {
        if (StringUtils.isNotBlank(this.phoneNumber)) this.phoneNumber = this.phoneNumber.replace("-", "");
        if (StringUtils.isNotBlank(this.birthDate)) this.birthDate = this.birthDate.replace("-", "");
    }

    public void setMemberRole(Role role) {
        this.role = role;
    }

    @Builder
    public MemberRequestDto(
            String name,
            String account,
            String password,
            String email,
            String phoneNumber,
            String certYn,
            String accountCertYn,
            String picture,
            Gender gender,
            String birthDate,
            Role role
    ) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accountCertYn = accountCertYn;
        this.certYn = certYn;
        this.picture = picture;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
    }
}
