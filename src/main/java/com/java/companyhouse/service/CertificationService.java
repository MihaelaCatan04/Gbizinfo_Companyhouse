package com.java.companyhouse.service;

import com.java.companyhouse.mapper.CertificationMapper;
import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificationService extends AbstractBatchService<CertificationDto> {

    private final CertificationMapper certificationMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<CertificationDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<CertificationDto> certifications = snapshot.getEntities();

        if (certifications.isEmpty()) {
            certificationMapper.softDeleteAllCompanyCertifications(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (CertificationDto dto : certifications) {
            certificationMapper.upsertCertification(dto);
            certificationMapper.upsertCompanyCertification(dto, syncId);
        }

        certificationMapper.softDeleteMissingCompanyCertifications(corporateNumber, syncId);
    }
}