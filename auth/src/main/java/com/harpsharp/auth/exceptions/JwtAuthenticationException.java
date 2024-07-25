package com.harpsharp.auth.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Builder
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final String code;
    private final String message;

    public JwtAuthenticationException(String msg, String code, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.message = msg;
    }

    public JwtAuthenticationException(String msg, String code) {
        super(msg);
        this.code = code;
        this.message = msg;
    }
}
