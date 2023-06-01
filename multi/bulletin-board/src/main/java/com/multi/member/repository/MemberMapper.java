package com.multi.member.repository;

import com.multi.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void signUp(Member member);

}
