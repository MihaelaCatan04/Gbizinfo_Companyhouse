package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.FinanceIdResolver;
import com.java.companyhouse.cache.MajorShareholderIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.MajorShareholderMapper;
import com.java.companyhouse.model.dto.MajorShareholderDto;
import com.java.companyhouse.model.receiver.FinanceMajorShareholderSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class MajorShareholderService extends AbstractNestedBatchService<FinanceMajorShareholderSnapshot, MajorShareholderDto> {

    private final MajorShareholderMapper majorShareholderMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final FinanceIdResolver financeIdResolver;
    private final MajorShareholderIdResolver majorShareholderIdResolver;

    public MajorShareholderService(MajorShareholderMapper majorShareholderMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, FinanceIdResolver financeIdResolver, MajorShareholderIdResolver majorShareholderIdResolver) {
        super(transactionTemplate);
        this.majorShareholderMapper = majorShareholderMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.financeIdResolver = financeIdResolver;
        this.majorShareholderIdResolver = majorShareholderIdResolver;
    }

    @Override
    protected String getMergeKey(MajorShareholderDto item) {
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
        majorShareholderMapper.softDeleteAllFinanceShareholders(companyId, financeId);
    }

    @Override
    protected void cleanup(String corporateNumber, String parentMergeKey, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long financeId = financeIdResolver.resolve(parentMergeKey);
        majorShareholderMapper.softDeleteMissingFinanceShareholders(companyId, financeId, syncId);
    }

    @Override
    protected void upsertAll(List<MajorShareholderDto> items, String parentMergeKey, String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long financeId = financeIdResolver.resolve(parentMergeKey);

        for (MajorShareholderDto dto : items) {
            majorShareholderMapper.upsertMajorShareholder(dto);
            Long majorShareholderId = majorShareholderIdResolver.resolve(dto.getMergeKey());
            majorShareholderMapper.upsertFinanceShareholder(companyId, financeId, majorShareholderId, syncId);
        }
    }

    public void deleteOrphanedFinanceShareholders(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        majorShareholderMapper.softDeleteOrphanedFinanceShareholders(companyId);
    }

    @Override
    protected String getCorporateNumber(MajorShareholderDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getParentMergeKey(MajorShareholderDto entity) {
        return entity.getFinanceMergeKey();
    }
}