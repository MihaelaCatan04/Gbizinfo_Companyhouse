package com.java.companyhouse.service;

import com.java.companyhouse.mapper.CertificationMapper;
import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService extends AbstractBatchService<CertificationDto> {

    private final CertificationMapper certificationMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<CertificationDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<CertificationDto> certifications = snapshot.getEntities();

        for (CertificationDto dto : certifications) {
            certificationMapper.upsertCertification(dto);
            certificationMapper.upsertCompanyCertification(dto);
        }

        if (certifications.isEmpty()) {
            certificationMapper.softDeleteAllCompanyCertifications(corporateNumber);
            return;
        }

        List<String> mergeKeys = certifications.stream().map(CertificationDto::getMergeKey).toList();

        certificationMapper.softDeleteMissingCompanyCertifications(corporateNumber, mergeKeys);
    }
}