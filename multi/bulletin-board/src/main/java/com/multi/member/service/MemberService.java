package com.multi.member.service;

import com.multi.member.dto.request.MemberRequestDto;

public interface MemberService {

    Long signUp(MemberRequestDto memberRequestDto);

}
