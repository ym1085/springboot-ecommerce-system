package com.shoppingmall.mapper;

import com.shoppingmall.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    int join(MemberVO member);

    void joinWithSocialLogin(MemberVO member);

    Optional<MemberVO> getMemberByEmail(@Param("email") String email, @Param("registrationId") String registrationId);

    Optional<MemberVO> getMemberById(Long memberId);

    Optional<MemberVO> getMemberByEmailWithSocialLogin(String email, String registrationId);

    void updateMemberByEmailAndPicture(MemberVO member);

    Optional<MemberVO> getMemberByAccount(String username);

    int checkDuplMemberAccount(String account);
}
