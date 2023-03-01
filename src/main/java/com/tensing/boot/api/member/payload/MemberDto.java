package com.tensing.boot.api.member.payload;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberDto {

    @Setter
    @Getter
    public static class MemberRequest {
        private String username;
        private String password;
        @Email
        private String email;

    }

    @Setter
    @Getter
    public static class PostResponse {
    }

    @Setter
    @Getter
    public static class PutRequest {
    }

    @Getter
    @Builder
    public static class PutResponse {
    }

}
