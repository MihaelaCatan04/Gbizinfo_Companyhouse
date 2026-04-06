package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ItemInfoMapper;
import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

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
    protected void processSnapshot(CompanySnapshot<ItemInfoDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<ItemInfoDto> itemInfos = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (itemInfos.isEmpty()) {
            deleteAllItems(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertItems(itemInfos, syncId);
        cleanupMissingItems(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<ItemInfoDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllItems(String corporateNumber) {
        itemInfoMapper.softDeleteAllCompanyItems(corporateNumber);
    }

    private void upsertItems(List<ItemInfoDto> itemInfos, String syncId) {
        for (ItemInfoDto dto : itemInfos) {
            if (dto == null) continue;
            itemInfoMapper.upsertItemInfo(dto);
            itemInfoMapper.upsertCompanyItem(dto, syncId);
        }
    }

    private void cleanupMissingItems(String corporateNumber, String syncId) {
        itemInfoMapper.softDeleteMissingCompanyItems(corporateNumber, syncId);
    }
}
