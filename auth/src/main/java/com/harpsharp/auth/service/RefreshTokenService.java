package com.harpsharp.auth.service;

import com.harpsharp.auth.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RefreshTokenService {

    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private static final String HASH_KEY = "RefreshToken";

    @Autowired
    public RefreshTokenService(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean existsByToken(String accessToken) {
        return redisTemplate.opsForHash().hasKey(HASH_KEY, accessToken);
    }

    public void deleteByToken(String accessToken) {
        redisTemplate.opsForHash().delete(HASH_KEY, accessToken);
    }

    public Optional<RefreshToken> findByToken(String accessToken) {
        RefreshToken refreshToken = (RefreshToken) redisTemplate.opsForHash().get(HASH_KEY, accessToken);
        return Optional.ofNullable(refreshToken);
    }

    public void save(RefreshToken refreshToken) {
        String accessToken = refreshToken.getAccess();
        redisTemplate.opsForHash().put(HASH_KEY, accessToken, refreshToken);
    }

    public void clear(){
        redisTemplate.delete(HASH_KEY);
    }
}
