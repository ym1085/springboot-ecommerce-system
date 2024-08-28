package com.shoppingmall.security.annotation;

import com.shoppingmall.security.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// https://any-ting.tistory.com/m/148
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "customUser";

    String password() default "password";

    String[] roles() default {"USER"};
}