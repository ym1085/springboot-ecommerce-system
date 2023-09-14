package com.multi.member.service;

import com.multi.member.dto.request.MemberRequestDto;

public interface MemberService {

    int signUp(MemberRequestDto memberRequestDto);

    int checkDuplMemberAccount(MemberRequestDto memberRequestDto);
}
