package com.multi.member.dto.request;

import com.multi.member.constant.Gender;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberRequestDto {
    private Long memberId;

    @NotBlank(message = "이름은 필수 입력 항목 입니다. 다시 시도해주세요.")
    private String name;

    @NotBlank(message = "ID는 필수 입력 항목 입니다. 다시 시도해주세요.")
    private String account;

    @NotBlank(message = "비밀번호는 필수 입력 항목 입니다. 다시 시도해주세요.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 항목입니다. 다시 시도해주세요.")
    @Email(message = "올바른 메일 형식이 아닙니다. 다시 시도해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다. 다시 시도해주세요.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 휴대폰번호 형식이 아닙니다. 다시 시도해주세요.")
    private String phoneNumber;

    @NotBlank(message = "사용자 인증에 실패하였습니다. 다시 시도해주세요.")
    @Pattern(regexp = "^Y$", message = "사용자 인증에 실패하였습니다. 다시 시도해주세요.")
    private String certYn;

    private String imageUrl;
    private Gender gender;

    @NotBlank(message = "생년월일은 필수 입력 항목입니다. 다시 시도해주세요.")
    private String birthDate;

    // 사용자가 회원 가입 요청 시, 해당 사용자의 비밀번호를 해싱(hashing), 변환 하기 위함
    public void encodeMemberPassword(PasswordEncoder passwordEncoder) {
        if (StringUtils.isEmpty(this.password)) {
            return;
        }
        this.password = passwordEncoder.encode(this.password);
    }

    // DB에 넣을때는 '-' 제거 후 넣는다
    public void replaceHyphen() {
        if (StringUtils.isNotBlank(this.phoneNumber)) this.phoneNumber = this.phoneNumber.replace("-", "");
        if (StringUtils.isNotBlank(this.birthDate)) this.birthDate = this.birthDate.replace("-", "");
    }
}
