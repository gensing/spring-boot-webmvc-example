package com.tensing.boot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class TokenProvider {
    private final int jwtExpirationInMs;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Key key;

    public TokenProvider(String secret, SignatureAlgorithm signatureAlgorithm, int jwtExpirationInMs) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Claims claims) {

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setHeaderParam("alg", signatureAlgorithm)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationInMs))
                .setClaims(claims)
                .signWith(signatureAlgorithm, key)
                .compact();
    }

    public Claims decodeToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

}
