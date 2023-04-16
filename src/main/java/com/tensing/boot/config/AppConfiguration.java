package com.tensing.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tensing.boot.global.security.provider.TokenProvider;
import com.tensing.boot.config.properties.JwtProperties;
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
        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 객체가 아닌 string 으로 표현
                .registerModules(new JavaTimeModule());
    }

}
