package com.harpsharp.auth.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserAlreadyExistsException extends RuntimeException {
    private final Integer code;
    private final String message;
    private final String details;
}