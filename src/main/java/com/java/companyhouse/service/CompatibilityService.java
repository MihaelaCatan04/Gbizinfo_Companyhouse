package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CompatibilityMapper;
import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
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
    protected void processSnapshot(CompanySnapshot<CompatibilityOfChildcareAndWorkDto> snapshot) {
        advisoryLockMapper.acquireAdvisoryLock(snapshot.getCorporateNumber());
        snapshot.getEntities().forEach(compatibilityMapper::upsertCompatibility);
    }
}