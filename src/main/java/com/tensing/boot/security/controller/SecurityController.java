package com.tensing.boot.security.controller;

import com.tensing.boot.security.payload.SecurityDto;
import com.tensing.boot.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/security")
@RequiredArgsConstructor
@RestController
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public SecurityDto.TokenResponse security(@RequestBody SecurityDto.TokenRequest tokenRequest) {
        return securityService.getToken(tokenRequest);
    }
}
