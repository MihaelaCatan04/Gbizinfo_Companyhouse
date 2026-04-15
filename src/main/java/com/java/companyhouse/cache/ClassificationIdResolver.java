package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.ClassificationMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClassificationIdResolver extends CachedIdResolver {
    private final ClassificationMapper classificationMapper;

    public ClassificationIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, ClassificationMapper classificationMapper) {
        super(redisTemplate, sharedIdL1Cache, "classification:merge_key:");
        this.classificationMapper = classificationMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return classificationMapper.findClassificationIdByMergeKey(mergeKey);
    }
}