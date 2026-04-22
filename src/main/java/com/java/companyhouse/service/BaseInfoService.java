package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.BaseInfoMapper;
import com.java.companyhouse.model.dto.BaseInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class BaseInfoService extends AbstractBatchService<BaseInfoDto> {

    static final String BASE_INFO_KEY = "base_info:id:";

    private final BaseInfoMapper baseInfoMapper;
    private final Cache cache;

    public BaseInfoService(BaseInfoMapper baseInfoMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.baseInfoMapper = baseInfoMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<BaseInfoDto> chunk) {
        baseInfoMapper.bulkUpsertBaseInfos(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(BASE_INFO_KEY, mergeKeys, baseInfoMapper::findBaseInfoIdsByMergeKeys);
    }

    public Map<String, Long> resolveBaseInfoIds(List<String> mergeKeys) {
        return cache.resolveAll(BASE_INFO_KEY, mergeKeys, baseInfoMapper::findBaseInfoIdsByMergeKeys);
    }

    @Override
    protected String getMergeKey(BaseInfoDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(BaseInfoDto entity) {
        return entity.getCorporateNumber();
    }
}