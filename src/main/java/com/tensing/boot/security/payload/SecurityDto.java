package com.tensing.boot.security.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SecurityDto {

    @Setter
    @Getter
    public static class TokenRequest {
        private String username;
        private String password;
    }

    @Getter
    @Builder
    public static class TokenResponse {
        private String accessToken;
    }

}
