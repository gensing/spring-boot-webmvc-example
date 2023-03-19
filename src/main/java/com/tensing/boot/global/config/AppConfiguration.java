package com.tensing.boot.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.global.config.properties.JwtProperties;
import com.tensing.boot.global.security.module.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class AppConfiguration {

    private final JwtProperties tensingProperties;

    @Bean
    public TokenProvider accessTokenProvider() {
        JwtProperties.TokenProperties jwt = tensingProperties.accessToken();
        TokenProvider tokenProvider = new TokenProvider(jwt.secretKey(), jwt.signatureAlgorithm(), jwt.expirationInMs());
        return tokenProvider;
    }

    @Bean
    public TokenProvider refreshTokenProvider() {
        JwtProperties.TokenProperties jwt = tensingProperties.refreshToken();
        TokenProvider tokenProvider = new TokenProvider(jwt.secretKey(), jwt.signatureAlgorithm(), jwt.expirationInMs());
        return tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
