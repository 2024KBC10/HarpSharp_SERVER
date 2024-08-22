package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.util.ResponseUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final ResponseUtils responseUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //path and method verify
        String accessToken = request.getHeader("Authorization");
        String requestUri = request.getRequestURI();

        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if(cookies == null) throw new IllegalArgumentException("INVALID_COOKIE");

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if(refresh == null) throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");

        if(jwtUtil.isExpired(refresh)) throw new IllegalArgumentException("REFRESH_TOKEN_EXPIRED");

        String category = jwtUtil.getCategory(refresh);

        if (accessToken == null) throw new IllegalArgumentException("ACCESS_TOKEN_IS_NULL");
        if (!category.equals("refresh")) throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");

        accessToken = accessToken.replace("Bearer ", "");

        boolean isExist = jwtUtil.existsByToken(accessToken);
        if (!isExist) throw new IllegalArgumentException("INVALID_ACCESS_TOKEN");


        jwtUtil.deleteByToken(accessToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        responseUtils.writeResponseEntity(response, HttpStatus.CREATED.value(), "LOGOUT_SUCCESS", "정상적으로 로그아웃 되었습니다.");
    }
}
