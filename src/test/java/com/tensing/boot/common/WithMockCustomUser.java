package com.tensing.boot.common;

import com.tensing.boot.global.filters.security.model.code.RoleCode;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1;

    RoleCode role() default RoleCode.USER;

}
