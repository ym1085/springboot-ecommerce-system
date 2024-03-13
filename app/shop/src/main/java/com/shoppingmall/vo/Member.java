package com.shoppingmall.vo;

import com.shoppingmall.constant.Gender;
import com.shoppingmall.constant.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Member {
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
    private String registrationId;
    private String providerToken;

    // 클라이언트가 소셜 로그인를 시도하는 사용자가 제공되는 정보(data)에 변경이 있으면
    // 해당 데이터를 받아서 DB에 실제 업데이트 하기 전 값 셋팅을 위해 사용
    public Member updateRenewalMember(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleCode() {
        return this.role.getCode();
    }

    @Builder
    public Member(
            Long memberId,
            String name,
            String account,
            String password,
            String email,
            String phoneNumber,
            String picture,
            String birthDate,
            String certYn,
            Role role,
            LocalDateTime createDate,
            LocalDateTime updateDate,
            Gender gender,
            String registrationId,
            String providerToken
    ) {
        this.memberId = memberId;
        this.name = name;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.birthDate = birthDate;
        this.certYn = certYn;
        this.role = role;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.gender = gender;
        this.registrationId = registrationId;
        this.providerToken = providerToken;
    }
}
