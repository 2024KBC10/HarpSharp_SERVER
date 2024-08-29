package com.harpsharp.infra_rds.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
    CLOUD("cloud"),
    AI("ai"),
    FULLSTACK("fullstack");

    private final String position;
}
