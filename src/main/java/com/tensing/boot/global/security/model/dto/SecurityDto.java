package com.tensing.boot.global.security.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecurityDto {

    @Getter
    @RequiredArgsConstructor
    public enum GranType {

        ISSUE,
        REFRESH;

        private static final Map<String, GranType> descMap = Stream.of(GranType.values()).collect(Collectors.toUnmodifiableMap(GranType::name, Function.identity()));

        @JsonCreator
        public static GranType from(String s) {
            var granType = descMap.get(s);
            return granType == null ? GranType.ISSUE : granType;
        }
    }

    @FieldNameConstants
    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenRequest {

        @Schema(description = "로그인 방식 설정 필드", defaultValue = "USER_INFO")
        private GranType grantType = GranType.ISSUE;
        private String username;
        private String password;
        private String refreshToken;
    }

    @FieldNameConstants
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }

    @FieldNameConstants
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private long id;

        public boolean checkId(Long checkId) {
            return checkId == null ? false : id == checkId;
        }
    }

}
