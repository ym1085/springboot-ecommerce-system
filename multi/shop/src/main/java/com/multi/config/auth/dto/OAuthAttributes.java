package com.multi.config.auth.dto;

import com.multi.member.constant.Role;
import com.multi.member.constant.SocialType;
import com.multi.member.domain.Member;
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
    private String email;
    private String picture;
    private String registrationId;
    private String providerToken;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String registrationId, String providerToken) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.registrationId = registrationId;
        this.providerToken = providerToken;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String providerToken) {
        log.debug("registrationId = {}, userNameAttributeName = {}, attribute = {}, providerToken = {}"
                , registrationId, userNameAttributeName, attributes, providerToken);

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
                .email((String) response.getOrDefault("email", ""))
                .picture((String) response.getOrDefault("profile_image", ""))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }

    private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String providerToken) {
        log.debug("properties = {}", attributes.get("properties"));
        log.debug("kakao_account = {}", attributes.get("kakao_account"));

        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String name = (String) properties.get("nickname");
        String email = (String) kakaoAccount.get("email");
        String picture = (String) properties.get("profile_image");

        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .registrationId(registrationId)
                .providerToken(providerToken)
                .build();
    }
}
