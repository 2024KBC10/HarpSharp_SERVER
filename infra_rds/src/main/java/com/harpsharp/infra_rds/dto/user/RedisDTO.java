package com.harpsharp.infra_rds.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RedisDTO {
    private final String key;   // access token
    private final String value; // refresh token
}
