package com.harpsharp.auth.exceptions;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage(), e);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                "이미 존재하는 유저입니다.");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(apiResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<ApiResponse> handlerExpiredJwtException(ExpiredJwtException e) {
        log.error(e.getMessage(), e);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                "유효하지 않은 토큰 입니다.");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(apiResponse);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    private ResponseEntity<ApiResponse> handlerJwtAuthenticationException(JwtAuthenticationException e) {
        log.error(e.getMessage(), e);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "유효하지 않은 토큰 입니다.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    private ResponseEntity<ApiResponse> handlerMalformedJwtException(MalformedJwtException e) {
        log.error(e.getMessage(), e);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "유효하지 않은 토큰 입니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ApiResponse> handlerIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                "유효하지 않은 값 입니다.");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(apiResponse);
    }

    @ExceptionHandler(IllegalAccessException.class)
    private ResponseEntity<ApiResponse> handlerIllegalAccessException(IllegalAccessException e) {
        log.error(e.getMessage(), e);
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                "유효하지 않은 접근 입니다.");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(apiResponse);
    }
}