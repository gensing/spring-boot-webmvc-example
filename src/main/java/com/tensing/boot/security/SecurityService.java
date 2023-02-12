package com.tensing.boot.security;

import com.tensing.boot.security.payload.TokenResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface SecurityService {

    // 등록
    public String signup();

    // 발급
    public String authentication();

    // 검증
    public UsernamePasswordAuthenticationToken authorization(String token);
}
