package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CertificationMapper;
import com.java.companyhouse.model.dto.CertificationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CertificationService extends AbstractBatchService<CertificationDto> {

    private final CertificationMapper certificationMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public CertificationService(CertificationMapper certificationMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.certificationMapper = certificationMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        certificationMapper.softDeleteAllCompanyCertifications(corporateNumber);
    }

    @Override
    protected void upsert(CertificationDto entity, String syncId) {
        certificationMapper.upsertCertification(entity);
        certificationMapper.upsertCompanyCertification(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        certificationMapper.softDeleteMissingCompanyCertifications(corporateNumber, syncId);
    }

    @Override
    protected String getCorporateNumber(CertificationDto entity) {
        return entity.getCorporateNumber();
    }
}