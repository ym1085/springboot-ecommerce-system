package com.multi.member.repository;

import com.multi.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    int signUp(Member member);

    void signUpWithSocialLogin(Member member);

    Optional<Member> getMemberByEmail(String email, String registrationId);

    Optional<Member> getMemberById(Long memberId);

    Optional<Member> getMemberByEmailWithSocialLogin(String email, String registrationId);

    void updateMemberByEmailAndPicture(Member member);

    Optional<Member> getMemberByAccount(String username);

    int checkDuplMemberAccount(Member member);
}
