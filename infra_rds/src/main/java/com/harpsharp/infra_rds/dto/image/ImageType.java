package com.harpsharp.infra_rds.dto.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    PROFILE("PROFILE"),
    POST("POST"),
    GENERATED("GENERATED");

    private final String status;
}