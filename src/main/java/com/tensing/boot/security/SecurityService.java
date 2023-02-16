package com.tensing.boot.security;

import com.tensing.boot.security.payload.SecurityDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface SecurityService {

    // 등록
    void signup(SecurityDto.SignupRequest signupRequest);

    // 발급
    SecurityDto.LoginResponse login(SecurityDto.LoginRequest loginRequest);

    // 검증
    UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationTokenByAccessToken(String token);
}
