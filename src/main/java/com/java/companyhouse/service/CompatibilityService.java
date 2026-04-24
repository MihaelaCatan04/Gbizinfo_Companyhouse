package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompatibilityMapper;
import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CompatibilityService extends AbstractBatchService<CompatibilityOfChildcareAndWorkDto> {

    static final String COMPATIBILITY_KEY = "compatibility:id:";

    private final CompatibilityMapper compatibilityMapper;
    private final Cache cache;

    public CompatibilityService(CompatibilityMapper compatibilityMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.compatibilityMapper = compatibilityMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<CompatibilityOfChildcareAndWorkDto> chunk) {
        compatibilityMapper.bulkUpsertCompatibilities(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPATIBILITY_KEY, mergeKeys, compatibilityMapper::findCompatibilityIdsByMergeKeys);
    }

    public Map<String, Long> resolveCompatibilityIds(List<String> mergeKeys) {
        return cache.resolveAll(COMPATIBILITY_KEY, mergeKeys, compatibilityMapper::findCompatibilityIdsByMergeKeys);
    }

    @Override
    protected String getMergeKey(CompatibilityOfChildcareAndWorkDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(CompatibilityOfChildcareAndWorkDto entity) {
        return entity.getCorporateNumber();
    }
}