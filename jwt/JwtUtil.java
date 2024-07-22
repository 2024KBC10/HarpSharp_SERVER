package com.harpsharp.auth.jwt;

import com.harpsharp.auth.entity.RefreshEntity;
import com.harpsharp.auth.repository.RefreshRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
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
    private final RefreshRepository refreshRepository;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret, RefreshRepository refreshRepository){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpireTime  = Duration.ofHours(1);
        this.refreshTokenExpireTime = Duration.ofDays(365);
        this.refreshTokenExpireSeconds = 24 * 60 * 60 * 365;
        this.refreshRepository = refreshRepository;
    }

    public Long getAccessTokenExpireTime() {
        return accessTokenExpireTime.toMillis();
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    public String getUsername(String token){
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

    public String createAccessToken(String username, String role){
        return createJwt("access", username, role, accessTokenExpireTime.toMillis());
    }


    public String createRefreshToken(String username, String role){
        return createJwt("refresh",username, role, refreshTokenExpireTime.toMillis());
    }

    protected String createJwt(String category, String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
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

    public void addRefreshEntity(String access, String refresh){
        RefreshEntity refreshEntity = RefreshEntity
                .builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();

        refreshRepository.save(refreshEntity);
    }

    public void deleteByAccess(String access){
        refreshRepository.deleteById(access);
    }

    public Boolean existsByAccess(String access){
        return refreshRepository.existsById(access);
    }

    public Optional<RefreshEntity> findByAccess(String access){
        return refreshRepository.findById(access);
    }
}
