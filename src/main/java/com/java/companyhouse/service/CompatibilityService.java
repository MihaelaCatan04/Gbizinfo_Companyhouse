package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CompatibilityMapper;
import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CompatibilityService extends AbstractBatchService<CompatibilityOfChildcareAndWorkDto> {

    private final CompatibilityMapper compatibilityMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public CompatibilityService(CompatibilityMapper compatibilityMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.compatibilityMapper = compatibilityMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void upsert(CompatibilityOfChildcareAndWorkDto entity, String syncId) {
        compatibilityMapper.upsertCompatibility(entity);
    }
}