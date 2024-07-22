package com.harpsharp.auth.controller;


import com.harpsharp.auth.entity.RefreshEntity;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.repository.RefreshRepository;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken= null;
        String accessToken = request.getHeader("access");
        Cookie[] cookies = request.getCookies();

        System.out.println(accessToken);

        if(cookies == null) {
            return new ResponseEntity<>("Your cookies are empty.", HttpStatus.BAD_REQUEST);
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken= cookie.getValue();
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

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccess = jwtUtil.createAccessToken(username, role);
        String newRefresh = jwtUtil.createRefreshToken(username, role);

        // DB에 존재하는 Refresh 토큰 삭제 후 재발급
        jwtUtil.deleteByAccess(accessToken);
        jwtUtil.addRefreshEntity(newAccess, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
