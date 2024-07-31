package com.harpsharp.auth.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedirectURI {
    REISSUE("http://localhost:8080/reissue"),
    DOCS_AUTH("http://docs/auth:8080");
    private final String uri;
}
