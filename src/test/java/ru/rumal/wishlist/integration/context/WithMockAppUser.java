package ru.rumal.wishlist.integration.context;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockAppUser {

    String id() default "1";

    String email() default "correct@user.com";

    String password() default "12345678";
}
