package com.shoppingmall.config.auth.attribute;

import com.shoppingmall.domain.Member;
import lombok.Getter;

import java.io.Serializable;

/**
 * session에 값을 저장하려면 직렬화(Serialization)가 필요한데
 * Member 엔티티의 경우 추후 변경 사항이 있을 수 있기 때문에
 * 직렬화를 하기 위한 별도의 SessionMember 클래스 생성
 */
@Getter
public class SessionMember implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionMember(Member memberResponseDto) {
        this.name = memberResponseDto.getName();
        this.email = memberResponseDto.getEmail();
        this.picture = memberResponseDto.getPicture();
    }
}
