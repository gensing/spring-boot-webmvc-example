package com.tensing.boot.api.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PostDto {

    @Setter
    @Getter
    public static class PostRequest {
        private String title;
        private String body;

        public Post to(long memberId) {
            return Post.builder()
                    .member(Member.builder().id(memberId).build())
                    .title(this.title)
                    .body(this.body)
                    .build();
        }
    }

    @Builder
    @Setter
    @Getter
    public static class PostResponse {
        private long id;
        private String writer;
        private String title;
        private String body;

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime writeDate;

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updateDate;

        public static PostDto.PostResponse of(Post post) {
            return PostResponse.builder()
                    .id(post.getId())
                    .writer(post.getMember().getUsername())
                    .title(post.getTitle())
                    .body(post.getBody())
                    .writeDate(post.getCreatedDate())
                    .updateDate(post.getUpdatedDate())
                    .build();
        }
    }

    @Setter
    @Getter
    public static class PostPutRequest {
        private String title;
        private String body;
    }

}
