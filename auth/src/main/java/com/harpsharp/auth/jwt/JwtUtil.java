package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.entity.RefreshToken;
import com.harpsharp.auth.exceptions.JwtAuthenticationException;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Duration accessTokenExpireTime;
    private final Duration refreshTokenExpireTime;
    @Getter
    private final int refreshTokenExpireSeconds;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret, RefreshTokenService refreshTokenService, ObjectMapper objectMapper){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshTokenService = refreshTokenService;
        this.accessTokenExpireTime  = Duration.ofHours(1);
        this.refreshTokenExpireTime = Duration.ofDays(365);
        this.refreshTokenExpireSeconds = 24 * 60 * 60 * 365;
        this.objectMapper = objectMapper;
    }

    public Long getAccessTokenExpireTime() {
        return accessTokenExpireTime.toMillis();
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Boolean isExpired(String accessToken, String refreshToken) {
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload().getExpiration().before(new Date())
                || refreshTokenService.existsByToken(refreshToken);
    }

    public String createAccessToken(String username, String role){
        return createJwt("Authorization", username, role, accessTokenExpireTime.toMillis());
    }


    public String createRefreshToken(String username, String role){
        return createJwt("refresh", username, role, refreshTokenExpireTime.toMillis());
    }

    protected String createJwt(String category, String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("username", username)
                .claim("category", category)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String category, String token) {
        Cookie cookie = new Cookie(category, token);
        cookie.setMaxAge(refreshTokenExpireSeconds);
        cookie.setHttpOnly(true);

        return cookie;
    }

    public ResponseEntity<ApiResponse> reissueToken(String username, String role, String oldAccessToken, String oldRefreshToken){

        if(oldAccessToken == null || oldRefreshToken == null)
            throw new IllegalArgumentException("TOKEN_IS_NULL");

        if(isExpired(oldAccessToken, oldRefreshToken))
            throw new JwtAuthenticationException(HttpStatus.UNAUTHORIZED.value(), "REFRESH_TOKEN_EXPIRED");

        if(refreshTokenService.existsByToken(oldAccessToken)){
            throw new JwtAuthenticationException(HttpStatus.UNAUTHORIZED.value(), "REFRESH_TOKEN_EXPIRED");
        }

        deleteByToken(oldAccessToken, oldRefreshToken);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "JWT_REISSUED_SUCCESSFULLY",
                "JWT 재발급에 성공하였습니다.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(publishToken(username, role))
                .body(apiResponse);
    }

    public <T> void responseLogin(HttpServletResponse response, String username, String role, T user) throws IOException {
        ResponseWithData<T> apiResponse = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "JWT_PUBLISHED_SUCCESSFULLY",
                "로그인에 성공하였습니다.",
                user);

        publishToken(username, role)
                .forEach((key, values)-> {
                    for(String value: values){
                        response.addHeader(key, value);
                    }
                });

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");

        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private HttpHeaders publishToken(String username, String role) {
        String newAccessToken = createAccessToken(username, role);
        String newRefreshToken = createRefreshToken(username, role);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + newAccessToken);

        Cookie refreshCookie = createCookie("refresh", newRefreshToken);
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Max-Age=%d; SameSite=None;",
                refreshCookie.getName(),
                refreshCookie.getValue(),
                refreshCookie.getPath(),
                refreshCookie.getMaxAge());

        headers.add(HttpHeaders.SET_COOKIE, cookieHeader);

        return headers;
    }

    public boolean existsByToken(String accessToken) {
        return refreshTokenService.existsByToken(accessToken);
    }

    public void deleteByToken(String accessToken, String refreshToken) {
        RefreshToken blackList = RefreshToken
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        refreshTokenService.save(blackList);
    }

    public Boolean isAuthor(String username, String token){
        return username.equals(getUsername(token));
    }
}
