package com.harpsharp.auth.jwt;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // SignatureException이 원인인 경우 처리
        Throwable cause = authException.getCause();
        if (cause instanceof SignatureException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 반환
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid JWT signature\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 일반적인 인증 오류
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }
    }
}
