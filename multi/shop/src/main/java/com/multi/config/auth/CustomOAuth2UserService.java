package com.multi.config.auth;

import com.multi.config.auth.dto.OAuthAttributes;
import com.multi.config.auth.dto.SessionMember;
import com.multi.member.domain.Member;
import com.multi.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
     * - 서비스 구분 코드, 현재 로그인 진행 중인 서비스를 구분하는 코드 저장
     * - 현재는 구글 로그인이지만 추후 카카오, 네이버 로그인 연동 시 구분 하기 위해 사용
     *
     * 'userNameAttribute'
     * - OAuth2 로그인 진행 시 키가 되는 피드값 -> PK와 같은 의미
     * - Google의 경우 기본적으로 코드 지원('sub'), 네이버 + 카카오 코드 지원 안함
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // [ google, '', '' ]

        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String providerToken = userRequest.getAccessToken().getTokenValue(); // token
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

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberMapper.getMemberByEmail(attributes.getEmail())
                .map(entity -> entity.updateRenewalMember(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        // 기존 회원
        if (StringUtils.isNotBlank(member.getEmail())) {
            if (!attributes.getEmail().equalsIgnoreCase(member.getEmail()) || !attributes.getPicture().equalsIgnoreCase(member.getPicture())) { // 정보 변경이 있을 경우만 UPDATE
                Long successId = memberMapper.updateMemberByEmailAndPicture(member);
                if (successId > 0) {
                    member = memberMapper.getMemberByEmailWithSocialLogin(attributes.getEmail()).orElse(new Member());
                }
            }
        }

        // 신규 회원
        if (StringUtils.isBlank(member.getEmail())){
            Long successId = memberMapper.signUpWithSocialLogin(member);
            if (successId > 0) {
                member = memberMapper.getMemberByEmailWithSocialLogin(attributes.getEmail()).orElse(new Member());
            }
        }

        return member;
    }
}
