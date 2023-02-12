package com.tensing.boot.security;

import com.tensing.boot.security.payload.SignInRequest;
import com.tensing.boot.security.payload.SignUpRequest;
import com.tensing.boot.security.payload.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SecurityController {

    private SecurityService securityService;

    @PostMapping("/signup")
    public TokenResponse signup(SignUpRequest signUpRequest) {
        String token = securityService.signup();
        return TokenResponse.builder().token(token).build();
    }

    @PostMapping("/login")
    public TokenResponse login(SignInRequest signInRequest) {
        String token = securityService.authentication();
        return TokenResponse.builder().token(token).build();
    }
}
