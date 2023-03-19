package com.tensing.boot.global.security.filter;

import com.tensing.boot.global.security.service.SecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
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

    private final SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String accessToken = resolveToken(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (StringUtils.hasText(accessToken)) {
            try {
                final Authentication authentication = securityService.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid access token");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String bearerToken) {
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) ? bearerToken.substring(7) : null;
    }
}
