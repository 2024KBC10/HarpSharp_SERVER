package com.harpsharp.auth.exceptions;

import com.harpsharp.infra_rds.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<ErrorResponse> handlerExpiredJwtException(ExpiredJwtException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("TOKEN_IS_EXPIRED")
                .message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    private ResponseEntity<ErrorResponse> handlerJwtAuthenticationException(JwtAuthenticationException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    private ResponseEntity<ErrorResponse> handlerMalformedJwtException(MalformedJwtException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INVALID_TOKEN")
                .message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handlerIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("IS_NULL")
                .message("유효하지 않은 값 입니다.")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalAccessException.class)
    private ResponseEntity<ErrorResponse> handlerIllegalAccessException(IllegalAccessException e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INVALID_ACCESS")
                .message("유효하지 않은 접근입니다.")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}