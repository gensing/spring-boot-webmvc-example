package com.tensing.boot.security.service;

import com.tensing.boot.security.payload.SecurityDto;
import org.springframework.security.core.Authentication;

public interface SecurityService {

    // 발급
    SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest);

    // 검증
    Authentication getAuthentication(String accessToken);
}
