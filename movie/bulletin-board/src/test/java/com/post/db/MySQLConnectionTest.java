package com.post.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;

//@TestPropertySource("classpath:application-${spring.profiles.active}.yaml")
@ActiveProfiles("dev")
@SpringBootTest
public class MySQLConnectionTest {

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Test
    @DisplayName("MySQL DB Connection 테스트")
    void testDBConnect() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(driverName);
        System.out.println("clazz.getName => " + clazz.getName());

        try(Connection conn = DriverManager.getConnection(url, userName, password)){
            System.out.println(conn); // 콘솔창에서 연결정보를 출력하여 확인한다.
        } catch (Exception e) {
            System.out.println("cannot connect to mysql server, e => " + e.getMessage());
        }
    }
}
