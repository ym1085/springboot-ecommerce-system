package com.shoppingmall.handler;

import com.shoppingmall.constant.CodeEnum;
import com.shoppingmall.constant.Role;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 001. Mybatis의 경우 Enum의 별다른 조작이 없으면 Enum 상수 값 자체를 VALUE로 인식
 * > https://www.holaxprogramming.com/2015/11/12/spring-boot-mybatis-typehandler/
 *
 * 그렇기에 TypeHandler<T> 를 사용하여 Enum 값의 조작이 필요, 해당 프로젝트에서는
 * USER('ROLE_USER') -> ROLE_USER를 DB에 저장하기 위해 사용한다
 *
 * 002. TypeHandler
 * -> Mybatis가 PreparedStatement에 파라미터를 설정하고 ResultSet에서 값을 가져올때마다
 * 적절한 자바 타입의 값을 가져오거나, INSERT 시에 PreparedStatement에 적절한 자바 타입의
 * 값을 Set 할 때 사용이 된다.
 *
 * -> 즉, DB의 데이터로 매핑될 때 / DB의 데이터가 Role 타입의 객체로 매핑될 때 동작
 *
 * 003. abstract 선언 이유
 * > https://goodgid.github.io/MyBatis-Handling-TypeHandler-Enum/
 *
 * -> 추상 클래스는 객체 생성(new)이 불가능한 클래스로 기능의 구현을 강제하는 것보다는
 * 하위 클래스에서 해당 추상 클래스를 상속함으로써 기능을 '이용'하고 '확장'하는데 목적을 가짐
 *
 * -> 추상 클래스는 추상 메서드가 있을 수도 있고, 없을수도 있으며
 * 일반 메서드만 포함할 수도 있다
 *
 * -> 이유는 Enum class를 핸들링하는 공통 핵심 로직을 CodeEnumTypeHandler에 선언하여
 * 각각의 Custom Handler(생성되는 enum이 대상)들이 CodeEnumTypeHandler을 상속하는 구조를 만들기위함
 * @param <E>
 */
//@MappedTypes(CodeEnum.class)
public abstract class CodeEnumTypeHandler <E extends Enum<E> & CodeEnum> implements TypeHandler<CodeEnum> {

    // type field는 Role(enum)을 의미하는 Class 객체
    private final Class<E> type;

    public CodeEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null!");
        }
        this.type = type;
    }

    /*
        setParameter
        -> Role 타입의 객체가 DB의 데이터로 매핑될 때 동작
        -> Component ===> DB 요청 과정중
            -> DB Query의 i번째 인자 값을 순수한 parameter가 아닌 parameter.getCode()한 값으로 지정
            -> getCode() signature는 Role의 @Overide getCode() 함수 사용
    */
    @Override
    public void setParameter(PreparedStatement ps, int i, CodeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    /*
        getResult
        -> DB의 데이터가 Role 타입의 객체로 매핑될 때(값을 가져올 때) 동작
        -> 해당 값을 우리가 사용하는 Enum Class로 알맞게 변경시켜 줄 함수
    */
    @Override
    public CodeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return getCodeEnum(rs.getString(columnName));
    }

    @Override
    public CodeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getCodeEnum(rs.getString(columnIndex));
    }

    @Override
    public CodeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getCodeEnum(cs.getString(columnIndex));
    }

    private CodeEnum getCodeEnum(String code) {
        try {
            if (code == null) {
                return null;
            }

            if (Role.class.equals(type)) {
                code = code.split("_").length == 2 ? code.split("_")[1].toUpperCase() : "";
            }

            CodeEnum[] enumConstants = (CodeEnum[]) type.getEnumConstants();
            for (CodeEnum codeEnum : enumConstants) {
                if (codeEnum.getCode().equals(code)) {
                    return codeEnum;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TypeException("Can't make enum object '" + type + "'", e);
        }
    }
}