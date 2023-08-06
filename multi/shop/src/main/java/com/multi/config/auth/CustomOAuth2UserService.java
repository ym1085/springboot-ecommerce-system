package com.multi.config.auth;

import com.multi.config.auth.dto.OAuthAttributes;
import com.multi.config.auth.dto.SessionMember;
import com.multi.member.domain.Member;
import com.multi.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberMapper memberMapper;
    private final HttpSession session;

    /**
     * OAuth2 user login
     *
     * @description 아래 함수에서 사용되는 중요 변수 해설
     *
     * 'registrationId'
     * - ex) [ 'google', 'naver', 'kakao', 'facebook' ]
     * - 서비스 구분 코드, 현재 로그인 진행 중인 서비스를 구분하는 코드 저장
     * - 현재는 구글 로그인이지만 추후 카카오, 네이버 로그인 연동 시 구분 하기 위해 사용
     *
     * 'userNameAttribute'
     * - OAuth2 로그인 진행 시 키가 되는 피드값 -> PK와 같은 의미
     * - Google의 경우 기본적으로 코드 지원('sub'), 네이버 + 카카오 코드 지원 안함
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("Start. LoginInfo - OAuth2.0, userInfo = {}", userRequest.getClientRegistration().getRegistrationId());
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String providerToken = userRequest.getAccessToken().getTokenValue();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttribute, oAuth2User.getAttributes(), providerToken);

        Member member = saveOrUpdate(attributes);
        SessionMember sessionMember = new SessionMember(member);
        session.setAttribute("LOGIN_SESSION_USER", sessionMember);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    /**
     * 소셜 로그인시에 회원 정보를 저장
     *
     * @param attributes 소셜 로그인을 수행하는 회원 정보
     * @return 소셜 로그인을 수행한 유저의 정보 반환
     */
    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member findMember = memberMapper.getMemberByEmail(attributes.getEmail(), attributes.getRegistrationId()).orElse(new Member());
        if (findMember.getEmail() != null
                && attributes.getEmail().equalsIgnoreCase(findMember.getEmail())
                && attributes.getRegistrationId().equalsIgnoreCase(findMember.getRegistrationId())) {

            if(isUpdateNameAndPictureCondition(findMember, attributes)) {
                findMember.updateRenewalMember(attributes.getName(), attributes.getPicture());
                memberMapper.updateMemberByEmailAndPicture(findMember);
            }
            return memberMapper.getMemberByEmailWithSocialLogin(attributes.getEmail(), attributes.getRegistrationId()).orElse(new Member());
        } else {
            Member savedMember = attributes.toEntity();
            memberMapper.signUpWithSocialLogin(savedMember);
            return memberMapper.getMemberByEmailWithSocialLogin(attributes.getEmail(), attributes.getRegistrationId()).orElse(new Member());
        }
    }

    private boolean isUpdateNameAndPictureCondition(Member findMember, OAuthAttributes attributes) {
        boolean flag = true;
        if (findMember.getPicture().equalsIgnoreCase(attributes.getPicture())
                && findMember.getName().equalsIgnoreCase(attributes.getName())) {
            flag = false;
        }
        return flag;
    }
}
