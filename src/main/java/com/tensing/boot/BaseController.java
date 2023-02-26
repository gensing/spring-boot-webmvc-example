package com.tensing.boot;

import com.tensing.boot.configuration.OpenApiConfiguration;
import com.tensing.boot.security.entity.RoleCode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BaseController {
    
    @GetMapping("/security")
    @Secured(value = RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public String security(@AuthenticationPrincipal long userId) {
        return "security userId: " + userId;
    }
}
