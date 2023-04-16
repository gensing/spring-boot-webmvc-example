package com.tensing.boot.global.security.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;


@FieldNameConstants
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "rtk", timeToLive = 300000)
public class RefreshTokenCache {
    @Id
    private long memberId;

    private String jwt;

    private LocalDateTime refreshTime;

    @Builder
    public RefreshTokenCache(long memberId, String jwt) {
        this.memberId = memberId;
        this.jwt = jwt;
        this.refreshTime = LocalDateTime.now();
    }

}
