package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
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
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //path and method verify
        String accessToken = request.getHeader("Authorization");
        String requestUri = request.getRequestURI();

        if(accessToken == null || !accessToken.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }


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

        //refresh null check
        if (refresh == null) {
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
            System.out.println("refresh is null");
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
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
            System.out.println("refresh is expired");
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
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
            System.out.println("category = " + category);
            return;
        }

        accessToken = accessToken.replace("Bearer ", "");

        //DB에 저장되어 있는지 확인
        boolean isExist = refreshTokenService.existsByToken(accessToken);

        if (!isExist) {
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
            System.out.println("refresh = " + refresh + " is invalid");
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshTokenService.deleteByToken(accessToken);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        ApiResponse apiResponse =
                new ApiResponse(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "LOGOUT_SUCCESS",
                        "정상적으로 로그아웃 되었습니다."
                );
        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.addCookie(cookie);

        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
        response.getWriter().flush();

    }
}
