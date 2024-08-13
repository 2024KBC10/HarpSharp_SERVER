package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.util.ResponseUtils;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseUtils responseUtils;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Throwable cause = authException.getCause();
        if (cause instanceof SignatureException) {
            responseUtils.writeResponseEntity(response, HttpStatus.UNAUTHORIZED.value(), "INVALID_ACCESS", "유효하지 않은 인가입니다.");
        } else {
            responseUtils.writeResponseEntity(response, HttpStatus.BAD_REQUEST.value(), "INVALID_ARGUMENS", "잘못된 접근입니다.");
        }
    }
}
