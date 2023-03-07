package com.tensing.boot.config.properties;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "tensing")
public record TensingProperties(
        JwtProperties jwtAccessToken,
        JwtProperties jwtRefreshToken
) {
    public record JwtProperties(
            String tokenName,
            String secretKey,
            int expirationInMs,
            SignatureAlgorithm signatureAlgorithm
    ) {
    }

}
