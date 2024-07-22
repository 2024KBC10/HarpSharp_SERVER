package com.harpsharp.auth.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAlreadyExistsException extends RuntimeException {
    private final String code;
    private final String message;
}