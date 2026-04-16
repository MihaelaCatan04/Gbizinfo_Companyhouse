package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class CachedIdResolver {

    private final RedisTemplate<String, String> redisTemplate;
    private final Cache<String, Long> l1Cache;
    private final String redisKeyPrefix;

    protected CachedIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> l1Cache, String redisKeyPrefix) {
        this.redisTemplate = redisTemplate;
        this.l1Cache = l1Cache;
        this.redisKeyPrefix = redisKeyPrefix;
    }

    protected abstract Long fetchFromDb(String key);

    public Long resolve(String key) {
        String fullKey = redisKeyPrefix + key;
        return l1Cache.get(fullKey, k -> {
            log.debug("L1 miss for key={}", fullKey);
            return loadId(key, fullKey);
        });
    }

    private Long loadId(String key, String fullKey) {
        Long cachedId = getFromRedis(fullKey);
        if (cachedId != null) return cachedId;

        Long id = getFromDb(key, fullKey);
        writeToRedis(fullKey, id);
        return id;
    }

    private Long getFromRedis(String fullKey) {
        try {
            String cached = redisTemplate.opsForValue().get(fullKey);
            if (cached != null) {
                log.debug("L2 hit for key={}", fullKey);
                return Long.parseLong(cached);
            }
        } catch (Exception e) {
            log.warn("Redis unavailable for key={}, falling back to DB", fullKey, e);
        }
        return null;
    }

    private Long getFromDb(String key, String fullKey) {
        log.debug("L2 miss, hitting DB for key={}", fullKey);
        Long id = fetchFromDb(key);
        if (id == null) {
            throw new IllegalStateException("No entity found for key=" + key + " prefix=" + redisKeyPrefix);
        }
        return id;
    }

    private void writeToRedis(String fullKey, Long id) {
        try {
            redisTemplate.opsForValue().set(fullKey, id.toString(), 7, TimeUnit.DAYS);
        } catch (Exception e) {
            log.warn("Failed to write key={} to Redis, continuing", fullKey, e);
        }
    }
}