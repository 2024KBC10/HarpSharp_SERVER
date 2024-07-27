package com.harpsharp.auth.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(timeToLive = 24 * 60 * 60 * 365)
public class RefreshEntity {

    @Id
    private Long userId;
    private String refresh;


    @Builder
    public RefreshEntity(Long userId, String refreshToken){
        this.userId  = userId;
        this.refresh = refreshToken;
    }
}
