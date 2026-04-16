package com.java.companyhouse.service;

import com.java.companyhouse.cache.CertificationIdResolver;
import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CertificationMapper;
import com.java.companyhouse.model.dto.CertificationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CertificationService extends AbstractBatchService<CertificationDto> {

    private final CertificationMapper certificationMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final CertificationIdResolver certificationIdResolver;

    public CertificationService(CertificationMapper certificationMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, CertificationIdResolver certificationIdResolver) {
        super(transactionTemplate);
        this.certificationMapper = certificationMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.certificationIdResolver = certificationIdResolver;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        certificationMapper.softDeleteAllCompanyCertifications(companyId);
    }

    @Override
    protected void upsert(CertificationDto entity, String syncId) {
        certificationMapper.upsertCertification(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long certificationId = certificationIdResolver.resolve(entity.getMergeKey());

        certificationMapper.upsertCompanyCertification(companyId, certificationId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        certificationMapper.softDeleteMissingCompanyCertifications(companyId, syncId);
    }

    @Override
    protected String getCorporateNumber(CertificationDto entity) {
        return entity.getCorporateNumber();
    }
}