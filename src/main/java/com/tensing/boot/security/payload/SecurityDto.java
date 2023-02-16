package com.tensing.boot.security.payload;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SecurityDto {

    @Setter
    @Getter
    public static class SignupRequest {
        private String username;
        private String password;
        @Email
        private String email;

    }

    public static class SignupResponse {
    }

    @Setter
    @Getter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @Builder
    public static class LoginResponse {
        private String accessToken;
    }

}
