package com.shoppingmall.dto.response;

import com.shoppingmall.constant.Gender;
import com.shoppingmall.constant.Role;
import com.shoppingmall.vo.Member;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
