package com.tensing.boot.global.security.service;

import com.tensing.boot.global.security.model.dto.SecurityDto;
import org.springframework.security.core.Authentication;

public interface SecurityService {

    // 발급
    SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest);

    SecurityDto.TokenResponse getToken(String username, String password);

    SecurityDto.TokenResponse getToken(String refreshToken);

    // 검증
    Authentication getAuthentication(String accessToken);

    Authentication getAuthenticationByBearerToken(String bearerToken);
}
