package com.tensing.boot.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration {

    private final SecurityService SecurityService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

        // 세션 사용 안함
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Http basic Auth 기반으로 로그인 인증창 미사용.
        http.httpBasic().disable();

        // form 기반 로그인 페이지 미사용.
        http.formLogin().disable();

        // csrf 보안 미사용.
        http.csrf().disable();

        // cors 필터 미사용
        http.cors().disable();

        // http.authorizeRequests().anyRequest().permitAll();

        // add jwt authorization filter
        //http.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore((req, res, chain) -> {

            final String authToken = ((HttpServletRequest) req).getHeader(HttpHeaders.AUTHORIZATION);

            final UsernamePasswordAuthenticationToken authentication = SecurityService.authorization(authToken);

            if (authentication != null) SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(req, res);

        }, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
