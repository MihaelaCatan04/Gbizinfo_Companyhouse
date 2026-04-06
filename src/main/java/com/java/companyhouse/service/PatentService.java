package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.PatentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class PatentService extends AbstractBatchService<PatentDto> {

    private final PatentMapper patentMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public PatentService(PatentMapper patentMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.patentMapper = patentMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        patentMapper.softDeleteAllCompanyPatents(corporateNumber);
    }

    @Override
    protected void upsert(PatentDto entity, String syncId) {
        patentMapper.upsertPatent(entity);
        patentMapper.upsertCompanyPatent(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        patentMapper.softDeleteMissingCompanyPatents(corporateNumber, syncId);
    }
}