package com.tensing.boot.global.security.impl;

import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.global.security.Const;
import com.tensing.boot.global.security.dao.RefreshTokenCashRepository;
import com.tensing.boot.global.security.provider.TokenProvider;
import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.exception.model.code.ErrorCode;
import com.tensing.boot.global.security.model.code.RoleCode;
import com.tensing.boot.global.security.model.data.RefreshTokenCache;
import com.tensing.boot.global.security.model.dto.SecurityDto;
import com.tensing.boot.global.security.service.SecurityService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final MemberService memberService;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;
    private final RefreshTokenCashRepository refreshTokenCashRepository;

    // 인증
    @Override
    public SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest) {
        return switch (loginRequest.getGrantType()) {
            case REFRESH -> getToken(loginRequest.getRefreshToken());
            default -> getToken(loginRequest.getUsername(), loginRequest.getPassword());
        };
    }

    // 권한 정보 구하기
    @Override
    public Authentication getAuthentication(String accessToken) {

        final Claims claims = accessTokenProvider.decodeToken(accessToken);

        // 정보가 없으면 에러를 내자
        final var id = claims.get("userId", Long.class);
        final var roles = claims.get("roles", List.class);

        if (id == null) {
            log.info("JWT userId is null.");
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

        final var userInfo = SecurityDto.UserInfo.builder().id(id).build();
        final var auths = roles != null ? roles.stream().map(i -> new SimpleGrantedAuthority(String.valueOf(i))).toList() : null;
        return new UsernamePasswordAuthenticationToken(userInfo, null, auths);
    }

    @Override
    public Authentication getAuthenticationByBearerToken(String bearerToken) {
        final var accessToken = resolveToken(bearerToken);
        return getAuthentication(accessToken);
    }

    private String resolveToken(String bearerToken) {
        if (!StringUtils.hasText(bearerToken) && bearerToken.startsWith(Const.BEARER_PREFIX))
            new BusinessException(ErrorCode.NOT_BEARER_TOKEN);
        return bearerToken.substring(7);
    }

    public SecurityDto.TokenResponse getToken(String username, String password) {

        final var memberEntity = Optional.ofNullable(memberService.findMember(username, password))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        final var descRoles = memberEntity.getRoles();
        final var roles = descRoles != null ? descRoles.stream().map(i -> RoleCode.getByDesc(i.getDesc()).getRoleName()).toList() : null;

        final var claim = Jwts.claims();
        claim.put("userId", memberEntity.getId());
        claim.put("roles", roles); // List.of(RoleCode.USER.getRoleName())

        claim.setSubject("atk");
        var atk = accessTokenProvider.generateToken(claim);

        claim.setSubject("rtk");
        var rtk = refreshTokenProvider.generateToken(claim);

        // redis 에 refresh token 저장.
        //refreshTokenRepository.deleteById(member.getId());
        refreshTokenCashRepository.save(RefreshTokenCache.builder().memberId(memberEntity.getId()).jwt(rtk).build());

        return SecurityDto.TokenResponse.builder()
                .accessToken(atk)
                .refreshToken(rtk)
                .build();
    }

    public SecurityDto.TokenResponse getToken(final String refreshToken) {

        final var claim = refreshTokenProvider.decodeToken(refreshToken);

        final var userId = claim.get("userId", Long.class);

        final var rtk = refreshTokenCashRepository.findById(userId)
                .orElseThrow(() -> new AccessDeniedException("not found session"));

        if (!refreshToken.equals(rtk.getJwt()))
            throw new AccessDeniedException("invalid refreshToken");

        claim.setSubject("atk");
        final var atk = accessTokenProvider.generateToken(claim);

        return SecurityDto.TokenResponse.builder()
                .accessToken(atk)
                .refreshToken(refreshToken)
                .build();
    }
}
