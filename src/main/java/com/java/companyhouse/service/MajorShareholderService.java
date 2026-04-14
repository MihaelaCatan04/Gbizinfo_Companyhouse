package com.java.companyhouse.service;

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

    public MajorShareholderService(MajorShareholderMapper majorShareholderMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.majorShareholderMapper = majorShareholderMapper;
        this.advisoryLockMapper = advisoryLockMapper;
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
        majorShareholderMapper.softDeleteAllFinanceShareholders(corporateNumber, parentMergeKey);
    }

    @Override
    protected void cleanup(String corporateNumber, String parentMergeKey, String syncId) {
        majorShareholderMapper.softDeleteMissingFinanceShareholders(corporateNumber, parentMergeKey, syncId);
    }

    @Override
    protected void upsertAll(List<MajorShareholderDto> items, String parentMergeKey, String corporateNumber, String syncId) {
        for (MajorShareholderDto dto : items) {
            majorShareholderMapper.upsertMajorShareholder(dto);
            majorShareholderMapper.upsertFinanceShareholder(dto.getMergeKey(), parentMergeKey, corporateNumber, syncId);
        }
    }

    public void deleteOrphanedFinanceShareholders(String corporateNumber) {
        majorShareholderMapper.softDeleteOrphanedFinanceShareholders(corporateNumber);
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