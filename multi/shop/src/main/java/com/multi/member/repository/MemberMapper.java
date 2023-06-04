package com.multi.member.repository;

import com.multi.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    Long signUp(Member member);

    Long signUpWithSocialLogin(Member member);

    Optional<Member> getMemberByEmail(String email);

    Optional<Member> getMemberById(Long memberId);

    Optional<Member> getMemberByEmailWithSocialLogin(String email);

    Long updateMemberByEmailAndPicture(Member member);

}
