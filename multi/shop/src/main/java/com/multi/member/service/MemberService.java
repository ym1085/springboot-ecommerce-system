package com.multi.member.service;

import com.multi.member.dto.request.MemberRequestDto;
import com.multi.member.dto.response.MemberResponseDto;

public interface MemberService {

    MemberResponseDto signUp(MemberRequestDto memberRequestDto);

}
