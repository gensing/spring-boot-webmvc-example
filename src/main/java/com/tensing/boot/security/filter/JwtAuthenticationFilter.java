package com.tensing.boot.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.security.payload.SecurityDto;
import com.tensing.boot.security.service.SecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 토큰 발급 필터
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper om;
    private final SecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        // filter -> AuthenticationManager -> provider ( 스프링 시큐리티 구조를 따라 갈려면 provider 에 인증 로직 구현 )

        // get request data
        var tokenRequest = om.readValue(request.getInputStream(), SecurityDto.TokenRequest.class);

        // 토큰 생성
        var tokenResponse = securityService.getToken(tokenRequest);

        // refresh token 을 세션에 넣어주기. httpOnly & secured

        // flush json
        response.getWriter().write(om.writeValueAsString(tokenResponse));
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(request.getServletPath().equals("/api/security/auth-filter"));
    }

}
