package com.tensing.boot.global.security.controller;

import com.tensing.boot.global.security.dto.SecurityDto;
import com.tensing.boot.global.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/security")
@RequiredArgsConstructor
@RestController
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.CREATED)
    public SecurityDto.TokenResponse security(@RequestBody SecurityDto.TokenRequest tokenRequest) {
        return securityService.getToken(tokenRequest);
    }
}
