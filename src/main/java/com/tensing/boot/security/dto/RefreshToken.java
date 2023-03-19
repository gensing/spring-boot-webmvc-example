package com.tensing.boot.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;



@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "rtk", timeToLive = 300000)
public class RefreshToken {
    @Id
    private long memberId;

    private String jwt;

    private LocalDateTime refreshTime;

    @Builder
    public RefreshToken(long memberId, String jwt) {
        this.memberId = memberId;
        this.jwt = jwt;
        this.refreshTime = LocalDateTime.now();
    }

}
