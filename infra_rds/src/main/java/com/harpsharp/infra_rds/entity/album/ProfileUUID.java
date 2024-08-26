package com.harpsharp.infra_rds.entity.album;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(timeToLive = 60 * 60)
public class ProfileUUID {

    @Id
    private String username;
    private String UUID;


    @Builder
    public ProfileUUID(String username, String UUID){
        this.username  = username;
        this.UUID = UUID;
    }
}
