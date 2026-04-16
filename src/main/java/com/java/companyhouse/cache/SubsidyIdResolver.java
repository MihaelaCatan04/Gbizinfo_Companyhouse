package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.SubsidyMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubsidyIdResolver extends CachedIdResolver {
    private final SubsidyMapper subsidyMapper;

    public SubsidyIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, SubsidyMapper subsidyMapper) {
        super(redisTemplate, sharedIdL1Cache, "subsidy:merge_key:");
        this.subsidyMapper = subsidyMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return subsidyMapper.findSubsidyIdByMergeKey(mergeKey);
    }
}