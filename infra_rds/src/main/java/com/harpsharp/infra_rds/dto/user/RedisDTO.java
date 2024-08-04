package com.harpsharp.auth.dto;

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
