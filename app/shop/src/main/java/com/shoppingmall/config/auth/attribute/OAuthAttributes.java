package com.shoppingmall.config.auth.attribute;

import com.shoppingmall.constant.Role;
import com.shoppingmall.constant.SocialType;
import com.shoppingmall.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String account;
    private String email;
    private String picture;
    private String registrationId;
    private String providerToken;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String account, String email, String picture, String registrationId, String providerToken) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.account = account;
        this.email = email;
        this.picture = picture;
        this.registrationId = registrationId;
        this.providerToken = providerToken;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String providerToken) {
        OAuthAttributes oAuthAttributes = null;
        SocialType socialType = SocialType.getSocialType(registrationId);
        switch (socialType) {
            case GOOGLE:
                oAuthAttributes = ofGoogle(registrationId, userNameAttributeName, attributes, providerToken);
                break;
            case NAVER:
                oAuthAttributes = ofNaver(registrationId, "id", attributes, providerToken);
                break;
            case KAKAO:
                oAuthAttributes = ofKakao(registrationId, userNameAttributeName, attributes, providerToken);
                break;
            default:
                log.warn("No matching type exists for social login");
        }
        return oAuthAttributes;
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String providerToken) {
        return OAuthAttributes.builder()
                .name((String) attributes.getOrDefault("name", ""))
                .account(registrationId + "_" + providerToken)
                .email((String) attributes.getOrDefault("email", ""))
                .picture((String) attributes.getOrDefault("picture", ""))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }

    private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String providerToken) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.getOrDefault("name", ""))
                .account(registrationId + "_" + providerToken)
                .email((String) response.getOrDefault("email", ""))
                .picture((String) response.getOrDefault("profile_image", ""))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }

    private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String providerToken) {
        Map<String, Object> properties = (Map<String, Object>) attributes.getOrDefault("properties", "");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.getOrDefault("kakao_account", "");
        String name = (String) properties.getOrDefault("nickname", "");
        String account = registrationId + "_" + providerToken;
        String email = (String) kakaoAccount.getOrDefault("email", "");
        String picture = (String) properties.getOrDefault("profile_image", "");

        return OAuthAttributes.builder()
                .name(name)
                .account(account)
                .email(email)
                .picture(picture)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }

    /* 유저 소셜 로그인 진행 -> 신규/기존 유저 구분 -> 회원 정보 DB 저장/업데이트 진행할 때 사용 */
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .account(account)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }
}
