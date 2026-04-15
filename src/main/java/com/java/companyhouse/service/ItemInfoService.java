package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.ItemInfoIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ItemInfoMapper;
import com.java.companyhouse.model.dto.ItemInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ItemInfoService extends AbstractBatchService<ItemInfoDto> {

    private final ItemInfoMapper itemInfoMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final ItemInfoIdResolver itemInfoIdResolver;

    public ItemInfoService(ItemInfoMapper itemInfoMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, ItemInfoIdResolver itemInfoIdResolver) {
        super(transactionTemplate);
        this.itemInfoMapper = itemInfoMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.itemInfoIdResolver = itemInfoIdResolver;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        itemInfoMapper.softDeleteAllCompanyItems(companyId);
    }

    @Override
    protected void upsert(ItemInfoDto entity, String syncId) {
        itemInfoMapper.upsertItemInfo(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long infoId = itemInfoIdResolver.resolve(entity.getMergeKey());

        itemInfoMapper.upsertCompanyItem(companyId, infoId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        itemInfoMapper.softDeleteMissingCompanyItems(companyId, syncId);
    }

    @Override
    protected String getCorporateNumber(ItemInfoDto entity) {
        return entity.getCorporateNumber();
    }
}