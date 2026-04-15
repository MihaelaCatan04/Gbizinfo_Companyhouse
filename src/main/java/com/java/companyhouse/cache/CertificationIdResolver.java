package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.CertificationMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CertificationIdResolver extends CachedIdResolver {
    private final CertificationMapper certificationMapper;

    public CertificationIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, CertificationMapper certificationMapper) {
        super(redisTemplate, sharedIdL1Cache, "certification:merge_key:");
        this.certificationMapper = certificationMapper;
    }

    @Override
    protected Long fetchFromDb(String mergeKey) {
        return certificationMapper.findCertificationIdByMergeKey(mergeKey);
    }
}