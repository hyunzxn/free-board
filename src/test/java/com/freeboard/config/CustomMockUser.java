package com.freeboard.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomMockSecurityContext.class)
public @interface CustomMockUser {

	String name() default "zun";

	String email() default "zun@test.com";

	String password() default "";
}
