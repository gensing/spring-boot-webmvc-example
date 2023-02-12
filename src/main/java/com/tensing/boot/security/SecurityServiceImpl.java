package com.tensing.boot.security;

import com.tensing.boot.global.advice.exception.BusinessException;
import com.tensing.boot.global.advice.exception.ErrorCode;
import com.tensing.boot.modules.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 가입
    @Override
    public String signup() {
        final String encPassword = passwordEncoder.encode("");
        // .save();
        return "토큰이 아니라 다른 정보를 리턴 필요";
    }

    // 입증
    @Override
    public String authentication() {
        final String encPassword = passwordEncoder.encode("");
        final String savedPassword = ""; // .findOne();

        // 에러 코드 생성 필요
        if (encPassword != savedPassword) throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);

        String jwt = ""; // 토큰 생성
        return jwt;
    }

    // 권한 부여
    @Override
    public UsernamePasswordAuthenticationToken authorization(String token) {

        if (token == null) return null;

        final Claims claims;

        try {
            claims = tokenProvider.decodeToken(token);
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
        }

        final Long id = claims.get("id", Long.class);
        final Collection<?> role = claims.get("role", Collection.class);

        if (id == null || role == null) throw new BusinessException(ErrorCode.INVALID_JWT);

        return new UsernamePasswordAuthenticationToken(id, "", AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString(role)));
    }
}
