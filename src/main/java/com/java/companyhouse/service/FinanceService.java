package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.FinanceIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.FinanceMapper;
import com.java.companyhouse.model.dto.FinanceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class FinanceService extends AbstractBatchService<FinanceDto> {

    private final FinanceMapper financeMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final FinanceIdResolver financeIdResolver;
    private final MajorShareholderService majorShareholderService;
    private final ManagementIndexService managementIndexService;

    public FinanceService(FinanceMapper financeMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, FinanceIdResolver financeIdResolver, MajorShareholderService majorShareholderService, ManagementIndexService managementIndexService) {
        super(transactionTemplate);
        this.financeMapper = financeMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.financeIdResolver = financeIdResolver;
        this.majorShareholderService = majorShareholderService;
        this.managementIndexService = managementIndexService;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        financeMapper.softDeleteAllCompanyFinances(companyId);
        majorShareholderService.deleteOrphanedFinanceShareholders(corporateNumber);
        managementIndexService.deleteOrphanedFinanceManagementIndexes(corporateNumber);
    }

    @Override
    protected void upsert(FinanceDto entity, String syncId) {
        financeMapper.upsertFinance(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long financeId = financeIdResolver.resolve(entity.getMergeKey());

        financeMapper.upsertCompanyFinance(companyId, financeId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        financeMapper.softDeleteMissingCompanyFinances(companyId, syncId);
        majorShareholderService.deleteOrphanedFinanceShareholders(corporateNumber);
        managementIndexService.deleteOrphanedFinanceManagementIndexes(corporateNumber);
    }

    @Override
    protected String getCorporateNumber(FinanceDto entity) {
        return entity.getCorporateNumber();
    }
}