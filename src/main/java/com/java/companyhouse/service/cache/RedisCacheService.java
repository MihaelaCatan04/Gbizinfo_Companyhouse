package com.java.companyhouse.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisCacheService {

    private final StringRedisTemplate redis;

    public RedisCacheService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public String get(String key) {
        try {
            return redis.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis GET failed key={}", key, e);
            return null;
        }
    }

    public void set(String key, Long value) {
        try {
            redis.opsForValue().set(key, value.toString());
        } catch (Exception e) {
            log.warn("Redis SET failed key={}", key, e);
        }
    }

    public List<String> multiGet(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<String> result = redis.opsForValue().multiGet(keys);
            return result != null ? result : Collections.nCopies(keys.size(), null);
        } catch (Exception e) {
            log.warn("Redis MGET failed", e);
            return Collections.nCopies(keys.size(), null);
        }
    }

    public void multiSet(Map<String, Long> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        try {
            Map<String, String> payload = values.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> String.valueOf(e.getValue()),
                            (a, b) -> b,
                            LinkedHashMap::new
                    ));

            redis.opsForValue().multiSet(payload);
        } catch (Exception e) {
            log.warn("Redis MSET failed", e);
        }
    }
}