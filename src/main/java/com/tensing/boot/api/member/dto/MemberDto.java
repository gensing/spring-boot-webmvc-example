package com.tensing.boot.api.member.dto;

import com.tensing.boot.api.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberDto {

    @Setter
    @Getter
    public static class MemberRequest {
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(min = 8, max = 12, message = "이름은 8~12 자리입니다.")
        private String username;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
        private String password;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email
        private String email;

    }

    @Builder
    @Setter
    @Getter
    public static class MemberGetResponse {
        private String username;
        private String email;

        public static MemberGetResponse of(Member member) {
            return MemberGetResponse.builder()
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .build();
        }
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
