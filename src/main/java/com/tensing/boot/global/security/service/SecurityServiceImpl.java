package com.tensing.boot.global.security.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.service.MemberService;
import com.tensing.boot.global.security.code.RoleCode;
import com.tensing.boot.global.security.dto.SecurityDto;
import com.tensing.boot.global.security.module.TokenProvider;
import com.tensing.boot.global.exception.code.ErrorCode;
import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.security.dto.RefreshToken;
import com.tensing.boot.global.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RefreshTokenRepository refreshTokenRepository;

    // 인증
    @Override
    public SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest) {
        return switch (loginRequest.getGrantType()) {
            case REFRESH_TOKEN -> getToken(loginRequest.getRefreshToken());
            default -> getToken(loginRequest.getUsername(), loginRequest.getPassword());
        };
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
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }

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


    private SecurityDto.TokenResponse getToken(String username, String password) {

        final Member member = Optional.ofNullable(memberService.findMember(username, password))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        final var descRoles = member.getRoles();
        final var roles = descRoles != null ? descRoles.stream().map(i -> RoleCode.getByDesc(i.getDesc()).getRoleName()).toList() : null;

        final Claims claim = Jwts.claims();
        claim.put("userId", member.getId());
        claim.put("roles", roles); // List.of(RoleCode.USER.getRoleName())

        claim.setSubject("atk");
        var atk = accessTokenProvider.generateToken(claim);

        claim.setSubject("rtk");
        var rtk = refreshTokenProvider.generateToken(claim);

        // redis 에 refresh token 저장.
        //refreshTokenRepository.deleteById(member.getId());
        refreshTokenRepository.save(RefreshToken.builder().memberId(member.getId()).jwt(rtk).build());

        return SecurityDto.TokenResponse.builder()
                .accessToken(atk)
                .refreshToken(rtk)
                .build();
    }

    private SecurityDto.TokenResponse getToken(final String refreshToken) {

        final var claim = refreshTokenProvider.decodeToken(refreshToken);

        var userId = claim.get("userId", Long.class);

        final var rtk = refreshTokenRepository.findById(userId)
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
