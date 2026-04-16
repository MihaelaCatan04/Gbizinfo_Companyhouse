package com.java.companyhouse.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompanyIdResolver extends CachedIdResolver {
    private final CompanyMapper companyMapper;

    public CompanyIdResolver(RedisTemplate<String, String> redisTemplate, Cache<String, Long> sharedIdL1Cache, CompanyMapper companyMapper) {
        super(redisTemplate, sharedIdL1Cache, "company:corporate_number:");
        this.companyMapper = companyMapper;
    }

    @Override
    protected Long fetchFromDb(String corporateNumber) {
        return companyMapper.findCompanyIdByCorporateNumber(corporateNumber);
    }
}
