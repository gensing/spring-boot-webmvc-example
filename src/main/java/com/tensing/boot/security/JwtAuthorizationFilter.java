package com.tensing.boot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 권한 부여 필터
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";

    private final String accessTokenName;
    private final SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String accessToken = resolveToken(request.getHeader(accessTokenName));

        if (StringUtils.hasText(accessToken)) {
            final UsernamePasswordAuthenticationToken authentication = securityService.getUsernamePasswordAuthenticationTokenByAccessToken(accessToken);
            if (authentication != null) SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String bearerToken) {
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) ? bearerToken.substring(7) : null;
    }
}
