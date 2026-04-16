package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.BaseInfoMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BaseInfoIdResolver extends CachedIdResolver {
    private final BaseInfoMapper baseInfoMapper;

    public BaseInfoIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, BaseInfoMapper baseInfoMapper) {
        super(redisTemplate, sharedIdL1Cache, "base_info:merge_key:");
        this.baseInfoMapper = baseInfoMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return baseInfoMapper.findBaseInfoIdByMergeKey(mergeKey);
    }
}
