package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.CommendationMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommendationIdResolver extends CachedIdResolver {
    private final CommendationMapper commendationMapper;

    public CommendationIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, CommendationMapper commendationMapper) {
        super(redisTemplate, sharedIdL1Cache, "commendation:merge_key:");
        this.commendationMapper = commendationMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return commendationMapper.findCommendationIdByMergeKey(mergeKey);
    }
}