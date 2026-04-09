package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.model.dto.FinanceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class FinanceService extends AbstractBatchService<FinanceDto> {

    private final FinanceMapper financeMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final MajorShareholderService majorShareholderService;
    private final ManagementIndexService managementIndexService;

    public FinanceService(FinanceMapper financeMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, MajorShareholderService majorShareholderService, ManagementIndexService managementIndexService) {
        super(transactionTemplate);
        this.financeMapper = financeMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.majorShareholderService = majorShareholderService;
        this.managementIndexService = managementIndexService;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        financeMapper.softDeleteAllCompanyFinances(corporateNumber);
        majorShareholderService.deleteOrphanedFinanceShareholders(corporateNumber);
        managementIndexService.deleteOrphanedFinanceManagementIndexes(corporateNumber);
    }

    @Override
    protected void upsert(FinanceDto entity, String syncId) {
        financeMapper.upsertFinance(entity);
        financeMapper.upsertCompanyFinance(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        financeMapper.softDeleteMissingCompanyFinances(corporateNumber, syncId);
        majorShareholderService.deleteOrphanedFinanceShareholders(corporateNumber);
        managementIndexService.deleteOrphanedFinanceManagementIndexes(corporateNumber);
    }
}