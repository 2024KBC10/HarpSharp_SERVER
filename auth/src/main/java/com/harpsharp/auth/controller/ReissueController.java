package com.harpsharp.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.dto.RedisDTO;
import com.harpsharp.auth.dto.response.ApiResponse;
import com.harpsharp.auth.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = null;
        String accessToken  = request.getHeader("Authorization");

        if(accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        accessToken = accessToken.substring("Bearer ".length());

        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            //return new ResponseEntity<>("Your cookies are empty.", HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.harpsharp.auth.dto.response.ApiResponse.builder()
                            .code("INVALID_TOKEN")
                            .message("Your cookies are empty.")
                            .build());
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.harpsharp.auth.dto.response.ApiResponse.builder()
                            .code("INVALID_TOKEN")
                            .message("Token is null.")
                            .build());
        }

        try{
            jwtUtil.isExpired(refreshToken);
        }catch(JwtException e){
            //return new ResponseEntity<>("Refresh token is expired", HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.harpsharp.auth.dto.response.ApiResponse.builder()
                            .code("INVALID_TOKEN")
                            .message("Token is expired.")
                            .build());
        }


        String category = jwtUtil.getCategory(refreshToken);

        if(!category.equals("refresh")){
            //return new ResponseEntity<>("Invalid token. Please ensure you are using a valid access token.", HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.harpsharp.auth.dto.response.ApiResponse.builder()
                        .code("INVALID_TOKEN")
                            .message("Invalid token. Please ensure you are using a valid access token.")
                            .build());
        }

        Boolean isExist = jwtUtil.existsById(jwtUtil.getUserId(accessToken));

        if(!isExist){
            //return new ResponseEntity<>("Refresh token does not exist", HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.harpsharp.auth.dto.response.ApiResponse.builder()
                            .code("INVALID_TOKEN")
                            .message("Token does not exist.")
                            .build());
        }

        Long   userId   = jwtUtil.getUserId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String role     = jwtUtil.getRole(refreshToken);

        String newAccess  = jwtUtil.createAccessToken (userId, username, role);
        String newRefresh = jwtUtil.createRefreshToken(userId, username, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        jwtUtil.deleteById(userId);
        jwtUtil.addRefreshEntity(userId, newRefresh);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        ObjectMapper mapper = new ObjectMapper();

        RedisDTO redisDTO = RedisDTO
                .builder()
                .key(newAccess)
                .value(newRefresh)
                .build();

        //return new ResponseEntity<>("The JWT has been successfully reissued.", HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.harpsharp.auth.dto.response.ApiResponse.builder()
                        .code("JWT_REISSUED_SUCCESSFULLY")
                        .message("The JWT has been successfully reissued.")
                        .build());
    }
}
