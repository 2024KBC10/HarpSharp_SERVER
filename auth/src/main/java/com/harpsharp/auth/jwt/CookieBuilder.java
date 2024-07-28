package com.harpsharp.auth.jwt;

import jakarta.servlet.http.Cookie;

public class CookieBuilder {
    private final String name;
    private final String value;
    private final Boolean httpOnly;
    private final Boolean secure;
    private final int maxAge;
    private final String path;

    private CookieBuilder(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.httpOnly = builder.httpOnly;
        this.secure = builder.secure;
        this.maxAge = builder.maxAge;
        this.path = builder.path;
    }

    public static class Builder {
        private final String name;
        private final String value;
        private Boolean httpOnly = false;
        private Boolean secure = false;
        private int maxAge = -1;
        private String path = "/";

        public Builder(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Builder httpOnly(Boolean httpOnly) {
            this.httpOnly = httpOnly;
            return this;
        }

        public Builder secure(Boolean secure) {
            this.secure = secure;
            return this;
        }

        public Builder maxAge(int maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public CookieBuilder build() {
            return new CookieBuilder(this);
        }
    }

    public Cookie toCookie() {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        return cookie;
    }
}

