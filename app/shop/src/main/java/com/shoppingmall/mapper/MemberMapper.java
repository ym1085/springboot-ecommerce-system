package com.shoppingmall.mapper;

import com.shoppingmall.vo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    int join(Member member);

    void joinWithSocialLogin(Member member);

    Optional<Member> getMemberByEmail(@Param("email") String email, @Param("registrationId") String registrationId);

    Optional<Member> getMemberById(Long memberId);

    Optional<Member> getMemberByEmailWithSocialLogin(String email, String registrationId);

    void updateMemberByEmailAndPicture(Member member);

    Optional<Member> getMemberByAccount(String username);

    int checkDuplicateMemberAccount(String account);
}
