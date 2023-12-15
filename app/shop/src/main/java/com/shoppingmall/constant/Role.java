package com.shoppingmall.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import com.shoppingmall.handler.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

public enum Role implements CodeEnum {
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "손님"),
    ADMIN("ROLE_ADMIN", "관리자"),
    ;

    private final String code;
    private final String title;

    Role(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
        @MappedTypes(Role.class)
        -> 해당 함수가 다룰 클래스(Class)를 명시하기 위한 어노테이션
        -> 간단히 말해서 Mybatis에게 TypeHandler가 처리할 Class가 Role 타입을 처리한다고 알려주는 역할의 어노테이션
     */
    @MappedTypes(Role.class)
    public static class TypeHandler extends CodeEnumTypeHandler<Role> {
        public TypeHandler() {
            super(Role.class);
        }
    }

    @Override
    @JsonValue // 자바 객체(Obj) -> Json = 직렬화 하는 경우 사용하는 어노테이션
    public String getCode() {
        return this.code.toUpperCase();
    }
}
