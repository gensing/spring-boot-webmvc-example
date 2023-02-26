package com.tensing.boot.configuration.properties;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "tensing")
public record TensingProperties(
        JwtProperties jwt
) {
    public record JwtProperties(
            String tokenName,
            String secretKey,
            int expirationInMs,
            SignatureAlgorithm signatureAlgorithm
    ) {
    }

}
