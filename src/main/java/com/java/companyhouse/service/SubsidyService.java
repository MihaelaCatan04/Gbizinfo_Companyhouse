package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.SubsidyMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class SubsidyService extends AbstractBatchService<SubsidyDto> {

    private final SubsidyMapper subsidyMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public SubsidyService(SubsidyMapper subsidyMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.subsidyMapper = subsidyMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        subsidyMapper.softDeleteAllCompanySubsidies(corporateNumber);
    }

    @Override
    protected void upsert(SubsidyDto entity, String syncId) {
        subsidyMapper.upsertSubsidy(entity);
        subsidyMapper.upsertCompanySubsidy(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        subsidyMapper.softDeleteMissingCompanySubsidies(corporateNumber, syncId);
    }
}