package com.tensing.boot.configuration;

import com.tensing.boot.configuration.properties.TensingProperties;
import com.tensing.boot.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class AppConfiguration {

    private final TensingProperties tensingProperties;

    @Bean
    public TokenProvider jwtTokenProvider() {
        TensingProperties.JwtProperties jwt = tensingProperties.jwt();
        TokenProvider tokenProvider = new TokenProvider(jwt.secretKey(), jwt.signatureAlgorithm(), jwt.expirationInMs());
        return tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }
}
