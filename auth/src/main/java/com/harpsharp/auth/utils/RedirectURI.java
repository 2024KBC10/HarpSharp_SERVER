package com.harpsharp.auth.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedirectURI {
    DOCS_AUTH("http://swagger-auth:8080");
    private final String uri;
}
