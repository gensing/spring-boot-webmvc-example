package com.tensing.boot.global.config.properties;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "tensing.jwt")
public record JwtProperties(
        TokenProperties accessToken,
        TokenProperties refreshToken
) {
    public record TokenProperties(
            String tokenName,
            String secretKey,
            int expirationInMs,
            SignatureAlgorithm signatureAlgorithm
    ) {
    }

}
