package com.harpsharp.auth.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedirectURI {
    REISSUE("http://localhost:8080/reissue");

    private final String uri;
}
