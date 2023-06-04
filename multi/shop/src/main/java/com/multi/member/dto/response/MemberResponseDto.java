package com.multi.member.dto.response;

import com.multi.member.constant.Gender;
import com.multi.member.constant.Role;
import com.multi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberResponseDto {
    private Long memberId;
    private String name;
    private String account;
    private String password;
    private String email;
    private String phoneNumber;
    private String picture;
    private String birthDate;
    private String certYn;
    private Role role;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Gender gender;

    public MemberResponseDto(Member member) {
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.account = member.getAccount();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.picture = member.getPicture();
        this.birthDate = member.getBirthDate();
        this.certYn = member.getCertYn();
        this.role = member.getRole();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
        this.gender = member.getGender();
    }
}
