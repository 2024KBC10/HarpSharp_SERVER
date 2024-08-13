package com.harpsharp.auth.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Builder
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final Integer code;
    private final String message;

    public JwtAuthenticationException(Integer code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.message = msg;
    }

    public JwtAuthenticationException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.message = msg;
    }
}
