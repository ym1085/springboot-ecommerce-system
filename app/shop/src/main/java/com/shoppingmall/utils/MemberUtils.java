package com.shoppingmall.utils;

import com.shoppingmall.constant.Role;
import com.shoppingmall.dto.request.MemberRequestDto;
import com.shoppingmall.exception.PasswordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberUtils {

    private final PasswordEncoder passwordEncoder;

    /**
     * binding mapping을 통해 validation을 수행하기는 하나, EMPTY, BLANK에
     * 대처 하기 위해 다시 한번 새로운 Builder를 만들어서 반환
     */
    public MemberRequestDto getMemberInfo(MemberRequestDto memberRequestDto) {
        return MemberRequestDto
                .builder()
                .name(isNotBlankMemberInfo(memberRequestDto.getName()) ? memberRequestDto.getName() : "")
                .account(isNotBlankMemberInfo(memberRequestDto.getAccount()) ? memberRequestDto.getAccount() : "")
                .password(isNotBlankMemberInfo(memberRequestDto.getPassword()) ? encryptMemberPassword(memberRequestDto.getPassword()) : "")
                .email(isNotBlankMemberInfo(memberRequestDto.getEmail()) ? memberRequestDto.getEmail() : "")
                .phoneNumber(isNotBlankMemberInfo(memberRequestDto.getPhoneNumber()) ? replaceHyphen(memberRequestDto.getPhoneNumber()) : "")
                .certYn(isNotBlankMemberInfo(memberRequestDto.getCertYn()) ? memberRequestDto.getCertYn() : "N")
                .accountCertYn(isNotBlankMemberInfo(memberRequestDto.getAccountCertYn()) ? memberRequestDto.getAccountCertYn() : "N")
                .birthDate(isNotBlankMemberInfo(memberRequestDto.getBirthDate()) ? replaceHyphen(memberRequestDto.getBirthDate()) : "")
                //.picture(isNotBlankMemberInfo(memberRequestDto.getPicture()) ? memberRequestDto.getPicture() : "")
                .role(Role.USER)
                .build();
    }

    private String replaceHyphen(String token) {
        return (StringUtils.isNotBlank(token)) ? token.replace("-", "") : token;
    }

    private String encryptMemberPassword(String password) {
        if (isBlankMemberInfo(password)) throw new PasswordNotFoundException();
        return passwordEncoder.encode(password);
    }

    private boolean isNotBlankMemberInfo(String token) {
        return StringUtils.isNotBlank(token);
    }

    private boolean isBlankMemberInfo(String token) {
        return StringUtils.isBlank(token);
    }
}


