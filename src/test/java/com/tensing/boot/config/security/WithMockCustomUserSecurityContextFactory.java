package com.tensing.boot.config.security;

import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final var securityContext = SecurityContextHolder.createEmptyContext();
        var userInfo = SecurityDto.UserInfo.builder().id(annotation.id()).build();
        final var authenticationToken
                = new UsernamePasswordAuthenticationToken(userInfo, null, Arrays.asList(new SimpleGrantedAuthority(annotation.role().getRoleName())));
        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }

}
