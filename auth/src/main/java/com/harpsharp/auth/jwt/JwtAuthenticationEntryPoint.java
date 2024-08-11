package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // SignatureException이 원인인 경우 처리
        Throwable cause = authException.getCause();
        if (cause instanceof SignatureException) {
            ApiResponse apiResponse =
                    new ApiResponse(
                            LocalDateTime.now(),
                            HttpStatus.UNAUTHORIZED.value(),
                            "INVALID_ACCESS",
                            "유효하지 않은 접근입니다."
                    );
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            String json = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(json);
            response.getWriter().flush();
        } else {
            ApiResponse apiResponse =
                    new ApiResponse(
                            LocalDateTime.now(),
                            HttpStatus.UNAUTHORIZED.value(),
                            "INVALID_TOKEN",
                            "유효하지 않은 토큰입니다."
                    );
            String json = objectMapper.writeValueAsString(apiResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            response.getWriter().flush();
        }
    }
}
