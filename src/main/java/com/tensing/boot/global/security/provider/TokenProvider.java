package com.tensing.boot.global.security.provider;

import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.exception.model.code.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
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

        final var now = new Date();
        final var end = new Date(now.getTime() + jwtExpirationInMs);

        // 이곳에서 시간을 담아줘야 발급마다 시크릿 정보가 바뀜 ( claim 이 항상 달라지므로 )
        claims.setIssuedAt(now);
        claims.setExpiration(end);

        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setHeaderParam("alg", signatureAlgorithm)
                .setIssuedAt(now) // 이 필드 역할 확인 필요
                .setExpiration(end) // 이 필드 역할 확인 필요
                .setClaims(claims)
                .signWith(signatureAlgorithm, key)
                .compact();
    }

    public Claims decodeToken(String token) {

        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException ex) {
            log.info("Invalid JWT token");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT token");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT token");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims string is empty.");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

    }

}
