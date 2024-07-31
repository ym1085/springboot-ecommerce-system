package com.shoppingmall.mapper;

import com.shoppingmall.dto.request.MemberSaveRequestDto;
import com.shoppingmall.vo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    int join(MemberSaveRequestDto member);

    void joinWithSocialLogin(Member member);

    Optional<Member> getMemberByEmail(@Param("email") String email, @Param("registrationId") String registrationId);

    Optional<Member> getMemberById(Integer memberId);

    Optional<Member> getSocialMember(String email, String registrationId);

    void updateSocialMemberLoginProfile(Member member);

    Optional<Member> getMemberByAccount(String username);

    int checkDuplicateMemberAccount(String account);
}
