package com.tensing.boot.application.post.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

public class PostDto {

    @FieldNameConstants
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class PostRequest {
        private String title;
        private String body;
    }

    @FieldNameConstants
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class PostResponse {
        private long id;
        private String writer;
        private String title;
        private String body;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createdDate;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updatedDate;
    }

    @FieldNameConstants
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class PostPutRequest {
        private String title;
        private String body;
    }

}
