package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.WomenActivityMapper;
import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class WomenActivityService extends AbstractBatchService<WomenActivityInfoDto> {

    static final String WOMEN_ACTIVITY_KEY = "women_activity:id:";

    private final WomenActivityMapper womenActivityMapper;
    private final Cache cache;

    public WomenActivityService(WomenActivityMapper womenActivityMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.womenActivityMapper = womenActivityMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<WomenActivityInfoDto> chunk) {
        womenActivityMapper.bulkUpsertWomenActivities(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(WOMEN_ACTIVITY_KEY, mergeKeys, womenActivityMapper::findWomenActivityIdsByMergeKeys);
    }

    public Map<String, Long> resolveWomenActivityIds(List<String> mergeKeys) {
        return cache.resolveAll(WOMEN_ACTIVITY_KEY, mergeKeys, womenActivityMapper::findWomenActivityIdsByMergeKeys);
    }

    @Override
    protected String getMergeKey(WomenActivityInfoDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(WomenActivityInfoDto entity) {
        return entity.getCorporateNumber();
    }
}