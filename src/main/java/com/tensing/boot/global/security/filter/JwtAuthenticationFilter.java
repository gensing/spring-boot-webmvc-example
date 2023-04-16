package com.tensing.boot.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.global.security.model.dto.SecurityDto;
import com.tensing.boot.global.security.service.SecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 토큰 발급 필터
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper om;
    private final SecurityService securityService;

    private final String JWT_ISSUE_ENDPOINT = "/api/oauth/tokens";
    private final String JWT_ISSUE_ENDPOINT_METHOD = "POST";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        // filter -> AuthenticationManager -> provider ( 스프링 시큐리티 구조를 따라 갈려면 provider 에 인증 로직 구현 )
        
        // get request data
        var tokenRequest = om.readValue(request.getInputStream(), SecurityDto.TokenRequest.class);

        // 토큰 생성
        var tokenResponse = securityService.getToken(tokenRequest);

        // refresh token 을 세션에 넣어주기. httpOnly & secured

        // flush json
        response.setStatus(HttpStatus.CREATED.value());
        response.getWriter().write(om.writeValueAsString(tokenResponse));
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // run - request.getServletPath() 로 값 들어옴
        // test - request.getPathInfo() 로 값 들어옴
        return !((JWT_ISSUE_ENDPOINT.equals(request.getServletPath()) || JWT_ISSUE_ENDPOINT.equals(request.getPathInfo())) && JWT_ISSUE_ENDPOINT_METHOD.equals(request.getMethod()));
    }

}
