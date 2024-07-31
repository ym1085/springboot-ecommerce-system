package com.shoppingmall.config.auth;

import com.shoppingmall.config.auth.attribute.OAuthAttributes;
import com.shoppingmall.config.auth.attribute.SessionMember;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.vo.Member;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String SESSION_NAME = "LOGIN_SESSION_USER";
    private final MemberMapper memberMapper;
    private final HttpSession session; // JWT 사용하게 되면 SESSION 사용은 필요없어짐

    /**
     * OAuth2 후처리 함수로 부가적인 정보 저장이 필요한 경우 아래 함수에서 처리
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registration로 어떤 OAuth로 로그인 했는지 확인 가능
        log.debug("userRequest => getClientRegistration = {}", userRequest.getClientRegistration());
        log.debug("userRequest => getAccessToken = {}", userRequest.getAccessToken());

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필을 받는다
        log.debug("userRequest => oAuth2User = {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String providerToken = userRequest.getAccessToken().getTokenValue();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttribute, oAuth2User.getAttributes(), providerToken);

        Member member = upsertMember(attributes);
        SessionMember sessionMember = new SessionMember(member);
        session.setAttribute(SESSION_NAME, sessionMember);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleCode())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );

        // new PrincipalDetails()로 변경 하는데 ERROR 발생해서 일단 주석 처리
        /*return new PrincipalDetails(
                member,
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );*/
    }

    /**
     * 사용자 소셜 로그인 시도 시, DB에 UPSERT 하는 함수
     * 사용자가 있는 경우 UPDATE, 그렇지 않으면 INSERT 수행
     */
    private Member upsertMember(OAuthAttributes attributes) {
        Optional<Member> findMember = memberMapper.getMemberByEmail(attributes.getEmail(), attributes.getRegistrationId());

        if (findMember.isPresent()) { // DB에 기존 소셜 로그인 회원이 존재 -> UPDATE 수행
            Member existMember = findMember.get();
            if(isRequiredSocialMemberProfile(existMember, attributes)) {
                existMember.updateSocialMemberProfile(attributes.getName(), attributes.getPicture());
                memberMapper.updateSocialMemberLoginProfile(existMember);
            }
            return existMember;
        } else {
            Member newMember = attributes.toEntity();
            memberMapper.joinWithSocialLogin(newMember);
            return newMember;
        }
    }

    private boolean isRequiredSocialMemberProfile(Member existingMember, OAuthAttributes attributes) {
        return !existingMember.getName().equalsIgnoreCase(attributes.getName()) ||
                !existingMember.getPicture().equalsIgnoreCase(attributes.getPicture());
    }
}
