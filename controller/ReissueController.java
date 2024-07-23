package com.harpsharp.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.dto.RedisDTO;
import com.harpsharp.auth.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "JWT 재발급 API", description = "Access Token 및 Refresh Token 재발급을 위한 API")
public class ReissueController {
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    @Operation(summary = "Access/Refresh 토큰 재발급")
    @ApiResponse(responseCode = "201", description = "", content = @Content(schema = @Schema(implementation = RedisDTO.class)))
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = null;
        String accessToken  = request.getHeader("access");
        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            return new ResponseEntity<>("Your cookies are empty.", HttpStatus.BAD_REQUEST);
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null){
            return new ResponseEntity<>("refresh token is null", HttpStatus.BAD_REQUEST);
        }

        try{
            jwtUtil.isExpired(refreshToken);
        }catch(JwtException e){
            return new ResponseEntity<>("refresh token is expired", HttpStatus.BAD_REQUEST);
        }


        String category = jwtUtil.getCategory(refreshToken);

        if(!category.equals("refresh")){
            return new ResponseEntity<>("Invalid token. Please ensure you are using a valid access token.", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = jwtUtil.existsByAccess(accessToken);

        if(!isExist){
            return new ResponseEntity<>("refresh token does not exist", HttpStatus.BAD_REQUEST);
        }

        Long   userId   = jwtUtil.getUserId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String role     = jwtUtil.getRole(refreshToken);

        String newAccess  = jwtUtil.createAccessToken (userId, username, role);
        String newRefresh = jwtUtil.createRefreshToken(userId, username, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        jwtUtil.deleteByAccess(accessToken);
        jwtUtil.addRefreshEntity(newAccess, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        ObjectMapper mapper = new ObjectMapper();

        RedisDTO redisDTO = RedisDTO
                .builder()
                .key(newAccess)
                .value(newRefresh)
                .build();

        return ResponseEntity.status(201).body(mapper.writeValueAsString(redisDTO));
    }
}
