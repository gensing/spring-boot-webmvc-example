package com.tensing.boot.security.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.service.MemberService;
import com.tensing.boot.common.entity.module.TokenProvider;
import com.tensing.boot.exception.code.ErrorCode;
import com.tensing.boot.exception.exception.BusinessException;
import com.tensing.boot.security.code.RoleCode;
import com.tensing.boot.security.payload.SecurityDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    // 인증
    @Override
    public SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest) {

        final Member member = memberService.getMemberByUserNameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());

        // 입력 정보에 해당하는 유저를 찾을 수 없음.
        if (member == null)
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);

        Claims claims = Jwts.claims().setSubject("access");
        claims.put("userId", member.getId());

        return SecurityDto.TokenResponse.builder()
                .accessToken(tokenProvider.generateToken(claims))
                .build();
    }

    // 권한 정보 구하기
    @Override
    public Authentication getAuthentication(String token) {

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

        final Long id = claims.get("userId", Long.class);

        if (id == null) {
            log.info("JWT userId is null.");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

        return new UsernamePasswordAuthenticationToken(id, null, List.of(new SimpleGrantedAuthority(RoleCode.USER.getRoleName())));
    }
}
