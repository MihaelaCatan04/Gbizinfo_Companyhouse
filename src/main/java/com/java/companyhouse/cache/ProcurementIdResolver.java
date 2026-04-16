package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.ProcurementMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProcurementIdResolver extends CachedIdResolver {
    private final ProcurementMapper procurementMapper;

    public ProcurementIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, ProcurementMapper procurementMapper) {
        super(redisTemplate, sharedIdL1Cache, "procurement:merge_key:");
        this.procurementMapper = procurementMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return procurementMapper.findProcurementIdByMergeKey(mergeKey);
    }
}