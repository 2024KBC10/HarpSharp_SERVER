package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }
        catch(AuthenticationException | JwtException e) {
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
        }
    }
}
