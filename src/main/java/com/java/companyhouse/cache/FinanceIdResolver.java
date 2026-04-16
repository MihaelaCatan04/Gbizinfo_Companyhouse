package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.FinanceMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FinanceIdResolver extends CachedIdResolver {
    private final FinanceMapper financeMapper;

    public FinanceIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, FinanceMapper financeMapper) {
        super(redisTemplate, sharedIdL1Cache, "finance:merge_key:");
        this.financeMapper = financeMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return financeMapper.findFinanceIdByMergeKey(mergeKey);
    }
}