package com.tensing.boot.security.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.service.MemberService;
import com.tensing.boot.common.module.TokenProvider;
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
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final MemberService memberService;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;


    // 인증
    @Override
    public SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest) {

        final Member member = Optional.ofNullable(memberService.findMember(loginRequest.getUsername(), loginRequest.getPassword()))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        final var descRoles = member.getRoles();
        final var roles = descRoles != null ? descRoles.stream().map(i -> RoleCode.getByDesc(i.getDesc()).getRoleName()).toList() : null;

        final Claims claims = Jwts.claims().setSubject("jwt");
        claims.put("userId", member.getId());
        claims.put("roles", roles); // List.of(RoleCode.USER.getRoleName())

        return SecurityDto.TokenResponse.builder()
                .accessToken(accessTokenProvider.generateToken(claims))
                .refreshToken(refreshTokenProvider.generateToken(claims))
                .build();
    }

    // 권한 정보 구하기
    @Override
    public Authentication getAuthentication(String accessToken) {

        final Claims claims;

        try {
            claims = accessTokenProvider.decodeToken(accessToken);
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
        } catch (Exception e){
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

        final var id = claims.get("userId", Long.class);
        final var roles = claims.get("roles", List.class);

        if (id == null) {
            log.info("JWT userId is null.");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

        final var auths = roles != null ? roles.stream().map(i -> new SimpleGrantedAuthority(String.valueOf(i))).toList() : null;

        return new UsernamePasswordAuthenticationToken(id, null, auths);
    }
}
