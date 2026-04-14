package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ManagementIndexMapper;
import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.model.receiver.FinanceManagementIndexSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class ManagementIndexService extends AbstractNestedBatchService<FinanceManagementIndexSnapshot, ManagementIndexDto> {

    private final ManagementIndexMapper managementIndexMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public ManagementIndexService(ManagementIndexMapper managementIndexMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.managementIndexMapper = managementIndexMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected String getMergeKey(ManagementIndexDto item) {
        return item.getMergeKey();
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void deleteAll(String corporateNumber, String parentMergeKey) {
        managementIndexMapper.softDeleteAllFinanceManagementIndexes(corporateNumber, parentMergeKey);
    }

    @Override
    protected void cleanup(String corporateNumber, String parentMergeKey, String syncId) {
        managementIndexMapper.softDeleteMissingFinanceManagementIndexes(corporateNumber, parentMergeKey, syncId);
    }

    @Override
    protected void upsertAll(List<ManagementIndexDto> items, String parentMergeKey, String corporateNumber, String syncId) {
        for (ManagementIndexDto dto : items) {
            managementIndexMapper.upsertManagementIndex(dto);
            managementIndexMapper.upsertFinanceManagement(dto.getMergeKey(), parentMergeKey, corporateNumber, syncId);
        }
    }

    public void deleteOrphanedFinanceManagementIndexes(String corporateNumber) {
        managementIndexMapper.softDeleteOrphanedFinanceManagementIndexes(corporateNumber);
    }

    @Override
    protected String getCorporateNumber(ManagementIndexDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getParentMergeKey(ManagementIndexDto entity) {
        return entity.getFinanceMergeKey();
    }
}