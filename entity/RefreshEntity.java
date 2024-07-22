package com.harpsharp.auth.entity;

import com.harpsharp.auth.jwt.JwtUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

//@Entity(name = "refreshTokens")
//@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(timeToLive = 24 * 60 * 60 * 365)
public class RefreshEntity {

    @Id
    private String access;
    private String refresh;


    @Builder
    public RefreshEntity(String accessToken, String refreshToken){
        this.access  = accessToken;
        this.refresh = refreshToken;
    }
}
