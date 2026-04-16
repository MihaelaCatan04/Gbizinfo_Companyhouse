package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.WomenActivityMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class WomenActivityInfoIdResolver extends CachedIdResolver {
    private final WomenActivityMapper womenActivityMapper;

    public WomenActivityInfoIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, WomenActivityMapper womenActivityMapper) {
        super(redisTemplate, sharedIdL1Cache, "women_activity_info:merge_key:");
        this.womenActivityMapper = womenActivityMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return womenActivityMapper.findWomenActivityIdByMergeKey(mergeKey);
    }
}