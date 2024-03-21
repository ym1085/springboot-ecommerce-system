package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.constant.Gender;
import com.shoppingmall.constant.Role;
import com.shoppingmall.vo.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto {
    private Integer memberId;
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

    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .account(member.getAccount())
                .password(member.getPassword())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .picture(member.getPicture())
                .birthDate(member.getBirthDate())
                .certYn(member.getCertYn())
                .role(member.getRole())
                .createDate(member.getCreateDate())
                .updateDate(member.getUpdateDate())
                .gender(member.getGender())
                .build();
    }
}
