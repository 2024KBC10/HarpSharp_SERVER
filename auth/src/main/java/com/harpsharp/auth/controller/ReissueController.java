package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReissueController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @GetMapping(value = "/api/v1/reissue")
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request)
    {
        String refreshToken = null;
        String accessToken  = request.getHeader("Authorization");

        if(accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("Illegal Access Token");
            throw new IllegalArgumentException("BAD_REQUEST_TOKEN");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            throw new IllegalArgumentException("BAD_REQUEST_COOKIE");
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null){
            throw new IllegalArgumentException("BAD_REQUEST_COOKIE");
        }

        if(jwtUtil.isExpired(accessToken, refreshToken))
            throw new JwtException("EXPIRED_TOKEN");

        String category = jwtUtil.getCategory(refreshToken);

        if(!category.equals("refresh")){
            System.out.println("Illegal Refresh Token");
            throw new IllegalArgumentException("BAD_REQUEST_TOKEN");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role     = jwtUtil.getRole(refreshToken);

        String newAccess  = jwtUtil.createAccessToken (username, role);
        String newRefresh = jwtUtil.createRefreshToken(username, role);

        jwtUtil.deleteByToken(accessToken, refreshToken);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + newAccess);

        Cookie refreshCookie = jwtUtil.createCookie("refresh", newRefresh);
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Max-Age=%d; SameSite=None;",
                refreshCookie.getName(),
                refreshCookie.getValue(),
                refreshCookie.getPath(),
                refreshCookie.getMaxAge());

        headers.add(HttpHeaders.SET_COOKIE, cookieHeader);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "JWT_REISSUED_SUCCESSFULLY",
                "JWT 재발급에 성공하였습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(apiResponse);
    }
}
