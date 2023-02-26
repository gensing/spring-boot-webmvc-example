package com.tensing.boot.security;

import com.tensing.boot.security.payload.SecurityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

}
