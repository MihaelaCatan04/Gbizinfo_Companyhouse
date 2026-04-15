package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.MajorShareholderMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MajorShareholderIdResolver extends CachedIdResolver {
    private final MajorShareholderMapper majorShareholderMapper;

    public MajorShareholderIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, MajorShareholderMapper majorShareholderMapper) {
        super(redisTemplate, sharedIdL1Cache, "major_shareholder:merge_key:");
        this.majorShareholderMapper = majorShareholderMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return majorShareholderMapper.findMajorShareholderIdByMergeKey(mergeKey);
    }
}