package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.ItemInfoMapper;
import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.model.solved.JunctionPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItemInfoService extends AbstractBatchService<ItemInfoDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String ITEM_INFO_KEY = "item_info:id:";

    private final ItemInfoMapper itemInfoMapper;
    private final CompanyMapper companyMapper;
    private final Cache cache;

    public ItemInfoService(ItemInfoMapper itemInfoMapper, CompanyMapper companyMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.itemInfoMapper = itemInfoMapper;
        this.companyMapper = companyMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<ItemInfoDto> chunk) {
        itemInfoMapper.bulkUpsertItemInfos(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(ITEM_INFO_KEY, mergeKeys, itemInfoMapper::findItemInfoIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(ITEM_INFO_KEY, mergeKeys, itemInfoMapper::findItemInfoIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionPair> pairs, String syncId) {
        itemInfoMapper.bulkUpsertCompanyItems(pairs, syncId);
    }

    @Override
    protected void cleanup(List<Long> companyIds, String syncId) {
        itemInfoMapper.softDeleteMissingCompanyItems(companyIds, syncId);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        List<Long> companyIds = new ArrayList<>(resolveCompanyIds(corporateNumbers).values());
        itemInfoMapper.softDeleteAllCompanyItems(companyIds);
    }

    @Override
    protected String getMergeKey(ItemInfoDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(ItemInfoDto entity) {
        return entity.getCorporateNumber();
    }
}