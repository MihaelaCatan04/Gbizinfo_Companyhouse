package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.FinanceIdResolver;
import com.java.companyhouse.cache.ManagementIndexIdResolver;
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
    private final CompanyIdResolver companyIdResolver;
    private final FinanceIdResolver financeIdResolver;
    private final ManagementIndexIdResolver managementIndexIdResolver;

    public ManagementIndexService(ManagementIndexMapper managementIndexMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, FinanceIdResolver financeIdResolver, ManagementIndexIdResolver managementIndexIdResolver) {
        super(transactionTemplate);
        this.managementIndexMapper = managementIndexMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.financeIdResolver = financeIdResolver;
        this.managementIndexIdResolver = managementIndexIdResolver;
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
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long financeId = financeIdResolver.resolve(parentMergeKey);
        managementIndexMapper.softDeleteAllFinanceManagementIndexes(companyId, financeId);
    }

    @Override
    protected void cleanup(String corporateNumber, String parentMergeKey, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long financeId = financeIdResolver.resolve(parentMergeKey);
        managementIndexMapper.softDeleteMissingFinanceManagementIndexes(companyId, financeId, syncId);
    }

    @Override
    protected void upsertAll(List<ManagementIndexDto> items, String parentMergeKey, String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long financeId = financeIdResolver.resolve(parentMergeKey);

        for (ManagementIndexDto dto : items) {
            managementIndexMapper.upsertManagementIndex(dto);
            Long managementIndexId = managementIndexIdResolver.resolve(dto.getMergeKey());
            managementIndexMapper.upsertFinanceManagement(companyId, financeId, managementIndexId, syncId);
        }
    }

    public void deleteOrphanedFinanceManagementIndexes(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        managementIndexMapper.softDeleteOrphanedFinanceManagementIndexes(companyId);
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