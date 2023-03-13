package com.tensing.boot.api.member.base;

import com.tensing.boot.config.OpenApiConfiguration;
import com.tensing.boot.security.code.RoleCode;
import com.tensing.boot.security.dto.SecurityDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/test")
@RequiredArgsConstructor
@RestController
public class TestController {
    @GetMapping("/role")
    @Secured(value = RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public String security(@AuthenticationPrincipal SecurityDto.UserInfo userInfo) {
        return "security userId: " + userInfo.getId();
    }
}
