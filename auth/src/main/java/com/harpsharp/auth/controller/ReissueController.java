package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.entity.RefreshToken;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReissueController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @GetMapping(value = "/reissue")
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = null;
        String accessToken  = request.getHeader("Authorization");

        if(accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            throw new IllegalArgumentException("Invalid cookies");
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null){
            throw new IllegalArgumentException("Invalid cookies");
        }

        try{
            jwtUtil.isExpired(refreshToken);
        }catch(JwtException e){
            throw new IllegalArgumentException("Invalid cookies");
        }


        String category = jwtUtil.getCategory(refreshToken);

        if(!category.equals("refresh")){
            throw new IllegalArgumentException("Invalid cookies");
        }

        Boolean isExist = refreshTokenService.existsByToken(accessToken);

        if(!isExist){
            throw new IllegalArgumentException("Invalid cookies");
        }

        Long   userId   = jwtUtil.getUserId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String role     = jwtUtil.getRole(refreshToken);

        String newAccess  = jwtUtil.createAccessToken (userId, username, role);
        String newRefresh = jwtUtil.createRefreshToken(userId, username, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        refreshTokenService.deleteByToken(accessToken);

        RefreshToken refreshEntity = RefreshToken
                .builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();

        refreshTokenService.save(refreshEntity);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));
        response.setStatus(HttpStatus.OK.value());

        HttpStatus status = (request.getMethod().equals("POST")) ? HttpStatus.CREATED : HttpStatus.OK;
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                status.value(),
                "JWT_REISSUED_SUCCESSFULLY",
                "The JWT has been successfully reissued.");
        return ResponseEntity
                .status(status)
                .body(apiResponse);
    }
}
