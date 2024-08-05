package com.harpsharp.auth.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Duration accessTokenExpireTime;
    private final Duration refreshTokenExpireTime;
    @Getter
    private final int refreshTokenExpireSeconds;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpireTime  = Duration.ofHours(1);
        this.refreshTokenExpireTime = Duration.ofDays(365);
        this.refreshTokenExpireSeconds = 24 * 60 * 60 * 365;
    }

    public Long getAccessTokenExpireTime() {
        return accessTokenExpireTime.toMillis();
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("user_id", Long.class);
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

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createAccessToken(Long userId, String username, String role){
        return createJwt("Authorization", userId, username, role, accessTokenExpireTime.toMillis());
    }


    public String createRefreshToken(Long userId, String username, String role){
        return createJwt("refresh", userId, username, role, refreshTokenExpireTime.toMillis());
    }

    protected String createJwt(String category, Long userId, String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("user_id", userId)
                .claim("username", username)
                .claim("category", category)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String category, String token) {
        return new CookieBuilder.Builder(category, token)
                .httpOnly(true)
                .secure(true)
                .maxAge(getRefreshTokenExpireSeconds())
                .build()
                .toCookie();
    }

    public Boolean isAuthor(String username, String token){
        return username.equals(getUsername(token));
    }
}
