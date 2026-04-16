package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.CompatibilityMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompatibilityIdResolver extends CachedIdResolver {
    private final CompatibilityMapper compatibilityMapper;

    public CompatibilityIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, CompatibilityMapper compatibilityMapper) {
        super(redisTemplate, sharedIdL1Cache, "compatibility:merge_key:");
        this.compatibilityMapper = compatibilityMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return compatibilityMapper.findCompatibilityIdByMergeKey(mergeKey);
    }
}