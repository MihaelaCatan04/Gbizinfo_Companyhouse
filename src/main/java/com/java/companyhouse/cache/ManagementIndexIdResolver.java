package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.ManagementIndexMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ManagementIndexIdResolver extends CachedIdResolver {
    private final ManagementIndexMapper managementIndexMapper;

    public ManagementIndexIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, ManagementIndexMapper managementIndexMapper) {
        super(redisTemplate, sharedIdL1Cache, "management_index:merge_key:");
        this.managementIndexMapper = managementIndexMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return managementIndexMapper.findManagementIndexIdByMergeKey(mergeKey);
    }
}