package woojooin.planit.global.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import woojooin.planit.global.security.dto.general.Token;

import java.time.Duration;

@Repository
public class TokenRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String refreshToken, Long userId, long expiration) {
        String key = "token:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofSeconds(expiration));
    }

    public Token findToken(Long id) {
        String key = "token:" + id;
        String refreshToken = (String) redisTemplate.opsForValue().get(key);
        if (refreshToken != null) {
            return new Token(id, refreshToken, null);
        }
        return null;
    }

    public void deleteToken(Long id) {
        String key = "token:" + id;
        redisTemplate.delete(key);
    }

    public boolean existsToken(Long id) {
        String key = "token:" + id;
        return redisTemplate.hasKey(key);
    }
}
