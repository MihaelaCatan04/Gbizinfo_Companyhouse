package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.ItemInfoMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ItemInfoIdResolver extends CachedIdResolver {
    private final ItemInfoMapper itemInfoMapper;

    public ItemInfoIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, ItemInfoMapper itemInfoMapper) {
        super(redisTemplate, sharedIdL1Cache, "item_info:merge_key:");
        this.itemInfoMapper = itemInfoMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return itemInfoMapper.findInfoIdByMergeKey(mergeKey);
    }
}