package com.harpsharp.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.RedisDTO;
import com.harpsharp.auth.dto.response.ApiResponse;
import com.harpsharp.auth.dto.response.ErrorResponse;
import com.harpsharp.auth.entity.RefreshToken;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.utils.BaseResponse;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReissueController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @RequestMapping(value = "/reissue", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PATCH})
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logic(request, response);
        HttpStatus status = (request.getMethod().equals("POST")) ? HttpStatus.CREATED : HttpStatus.OK;
        return BaseResponse.withCode("JWT_REISSUED_SUCCESSFULLY", "The JWT has been successfully reissued.", status);
    }

    private void logic(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    }
}
