package com.shoppingmall.config.auth;

import com.shoppingmall.config.auth.dto.OAuthAttributes;
import com.shoppingmall.config.auth.dto.SessionMember;
import com.shoppingmall.member.domain.Member;
import com.shoppingmall.member.repository.MemberMapper;
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

    private static final String SESSION_NAME = "LOGIN_SESSION_USER";
    private final MemberMapper memberMapper;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("start. LoginInfo - OAuth2.0, userInfo = {}", userRequest.getClientRegistration().getRegistrationId());
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
        session.setAttribute(SESSION_NAME, sessionMember);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    /**
     * save member information at social login
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
        if (findMember.getPicture().equalsIgnoreCase(attributes.getPicture()) && findMember.getName().equalsIgnoreCase(attributes.getName())) {
            flag = false;
        }
        return flag;
    }
}
