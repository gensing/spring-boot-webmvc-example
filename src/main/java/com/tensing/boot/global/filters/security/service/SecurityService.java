package com.tensing.boot.global.filters.security.service;

import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import org.springframework.security.core.Authentication;

public interface SecurityService {

    // 발급
    SecurityDto.TokenResponse getToken(SecurityDto.TokenRequest loginRequest);

    // 검증
    Authentication getAuthentication(String accessToken);
}
