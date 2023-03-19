package com.tensing.boot.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "spring commerce API 명세서",
                description = "API 명세서",
                version = "v1"
        )
)
@SecurityScheme(
        name = OpenApiConfiguration.API_SCHEME_NAME_001,
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        paramName = HttpHeaders.AUTHORIZATION,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@Configuration
public class OpenApiConfiguration {
    public static final String API_SCHEME_NAME_001 = "api_key";

}
