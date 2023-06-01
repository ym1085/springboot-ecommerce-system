package com.multi.member.domain;

import com.multi.member.constant.Gender;
import com.multi.member.dto.request.MemberRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Member {
    private Long memberId;
    private String name;
    private String account;
    private String password;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private String birthDate;
    private String certYn;
    private String roleName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Gender gender;

    // Dto -> Entity(vo)
    public Member(MemberRequestDto memberRequestDto) {
        this.name = memberRequestDto.getName();
        this.account = memberRequestDto.getAccount();
        this.password = memberRequestDto.getPassword();
        this.email = memberRequestDto.getEmail();
        this.phoneNumber = memberRequestDto.getPhoneNumber();
        this.imageUrl = memberRequestDto.getImageUrl();
        this.birthDate = memberRequestDto.getBirthDate();
        this.certYn = memberRequestDto.getCertYn();
        this.gender = memberRequestDto.getGender();
    }
}
