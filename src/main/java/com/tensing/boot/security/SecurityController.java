package com.tensing.boot.security;

import com.tensing.boot.security.entity.RoleCode;
import com.tensing.boot.security.payload.SecurityDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody SecurityDto.SignupRequest signupRequest) {
        securityService.signup(signupRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SecurityDto.LoginResponse login(@RequestBody SecurityDto.LoginRequest loginRequest) {
        return securityService.login(loginRequest);
    }

    @SecurityRequirement(name = "api_key")
    @PostMapping("/security")
    @Secured(value = RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String security(@AuthenticationPrincipal long userId) {
        return "security userId: " + userId;
    }
}
