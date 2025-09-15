package konkuk.chacall.global.common.security.oauth2.tokenstorage;

import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.global.common.exception.JsonParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLoginTokenStorage implements LoginTokenStorage {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "auth:login-token:";

    @Override
    public void put(String key, Role role, String token, Duration ttl) {
        String redisKey = toRedisKey(key);
        Entry entry = new Entry(role, token);

        redisTemplate.opsForValue().set(redisKey, entry, ttl);
    }

    @Override
    public Entry consume(String key) {
        String redisKey = toRedisKey(key);
        Object value = redisTemplate.opsForValue().getAndDelete(redisKey);
        if (value == null) {
            return null;
        }

        if (value instanceof Entry entry) {
            return entry;
        }

        throw new JsonParsingException();
    }

    private String toRedisKey(String key) {
        return PREFIX + key;
    }
}