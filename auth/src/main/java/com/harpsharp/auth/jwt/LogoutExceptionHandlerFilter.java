package com.harpsharp.auth.jwt;

import com.harpsharp.infra_rds.util.ResponseUtils;
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

@RequiredArgsConstructor
public class LogoutExceptionHandlerFilter extends OncePerRequestFilter {
    private final ResponseUtils responseUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }
        catch(AuthenticationException | JwtException e) {
            responseUtils.writeResponseEntity(response, HttpStatus.UNAUTHORIZED.value(), e.getMessage(), "유효하지 않은 접근입니다.");
        }
        catch(IllegalArgumentException e){
            responseUtils.writeResponseEntity(response, HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getMessage());
        }
    }
}
