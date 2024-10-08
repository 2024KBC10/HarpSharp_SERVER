package com.harpsharp.auth.jwt;

import com.harpsharp.infra_rds.util.ResponseUtils;
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


@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final ResponseUtils responseUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
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

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if(cookies == null) throw new IllegalArgumentException("INVALID_COOKIE");

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null) throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");

        if(jwtUtil.isExpired(accessToken, refreshToken)) throw new IllegalArgumentException("REFRESH_TOKEN_EXPIRED");

        String category = jwtUtil.getCategory(refreshToken);

        if (accessToken == null) throw new IllegalArgumentException("ACCESS_TOKEN_IS_NULL");
        if (!category.equals("refresh")) throw new IllegalArgumentException("INVALID_REFRESH_TOKEN");

        accessToken = accessToken.replace("Bearer ", "");

        jwtUtil.deleteByToken(accessToken, refreshToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        responseUtils.writeResponseEntity(response, HttpStatus.CREATED.value(), "LOGOUT_SUCCESS", "정상적으로 로그아웃 되었습니다.");
    }
}
