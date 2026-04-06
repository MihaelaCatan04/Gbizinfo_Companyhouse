package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ItemInfoMapper;
import com.java.companyhouse.model.dto.ItemInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ItemInfoService extends AbstractBatchService<ItemInfoDto> {

    private final ItemInfoMapper itemInfoMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public ItemInfoService(ItemInfoMapper itemInfoMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.itemInfoMapper = itemInfoMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        itemInfoMapper.softDeleteAllCompanyItems(corporateNumber);
    }

    @Override
    protected void upsert(ItemInfoDto entity, String syncId) {
        itemInfoMapper.upsertItemInfo(entity);
        itemInfoMapper.upsertCompanyItem(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        itemInfoMapper.softDeleteMissingCompanyItems(corporateNumber, syncId);
    }
}