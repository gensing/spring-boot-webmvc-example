package com.tensing.boot.security;

import com.tensing.boot.global.advice.exception.BusinessException;
import com.tensing.boot.global.advice.exception.ErrorCode;
import com.tensing.boot.modules.TokenProvider;
import com.tensing.boot.security.entity.Member;
import com.tensing.boot.security.payload.SecurityDto;
import com.tensing.boot.security.repository.MemberRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 가입
    @Override
    public void signup(SecurityDto.SignupRequest signupRequest) {
        Member member = Member.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();
        memberRepository.save(member);
    }

    // 인증
    @Override
    public SecurityDto.LoginResponse login(SecurityDto.LoginRequest loginRequest) {

        final Member member = memberRepository.findByUsername(loginRequest.getUsername());

        if (member == null)
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);

        // 에러 코드 생성 필요
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);

        Claims claims = Jwts.claims().setSubject("access");
        claims.put("userId", member.getId());

        final String accessToken = tokenProvider.generateToken(claims);

        return SecurityDto.LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    // 권한 부여
    @Override
    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationTokenByAccessToken(String token) {

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

        return new UsernamePasswordAuthenticationToken(id, null, Stream.of(new SimpleGrantedAuthority("ROLE_USER")).collect(Collectors.toList()));
    }
}
