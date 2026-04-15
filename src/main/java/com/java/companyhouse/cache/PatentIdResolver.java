package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.PatentMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PatentIdResolver extends CachedIdResolver {
    private final PatentMapper patentMapper;

    public PatentIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, PatentMapper patentMapper) {
        super(redisTemplate, sharedIdL1Cache, "patent:merge_key:");
        this.patentMapper = patentMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return patentMapper.findPatentIdByMergeKey(mergeKey);
    }
}