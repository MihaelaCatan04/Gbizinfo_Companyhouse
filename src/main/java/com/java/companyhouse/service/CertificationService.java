package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CertificationMapper;
import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

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
    protected void processSnapshot(CompanySnapshot<CertificationDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<CertificationDto> certifications = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (certifications.isEmpty()) {
            deleteAllCertifications(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertCertifications(certifications, syncId);
        cleanupMissingCertifications(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<CertificationDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllCertifications(String corporateNumber) {
        certificationMapper.softDeleteAllCompanyCertifications(corporateNumber);
    }

    private void upsertCertifications(List<CertificationDto> certifications, String syncId) {
        for (CertificationDto dto : certifications) {
            if (dto == null) continue;
            certificationMapper.upsertCertification(dto);
            certificationMapper.upsertCompanyCertification(dto, syncId);
        }
    }

    private void cleanupMissingCertifications(String corporateNumber, String syncId) {
        certificationMapper.softDeleteMissingCompanyCertifications(corporateNumber, syncId);
    }
}
