package com.harpsharp.infra_rds.dto.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImagePath {
    PROFILE("/profile/"),
    POST("/post/"),
    GENERATED("/generated/");

    private final String path;
}
