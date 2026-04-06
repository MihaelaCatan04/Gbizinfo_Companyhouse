package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.SubsidyMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

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
    protected void processSnapshot(CompanySnapshot<SubsidyDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<SubsidyDto> subsidies = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (subsidies.isEmpty()) {
            deleteAllSubsidies(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertSubsidies(subsidies, syncId);
        cleanupMissingSubsidies(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<SubsidyDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllSubsidies(String corporateNumber) {
        subsidyMapper.softDeleteAllCompanySubsidies(corporateNumber);
    }

    private void upsertSubsidies(List<SubsidyDto> subsidies, String syncId) {
        for (SubsidyDto dto : subsidies) {
            if (dto == null) continue;
            subsidyMapper.upsertSubsidy(dto);
            subsidyMapper.upsertCompanySubsidy(dto, syncId);
        }
    }

    private void cleanupMissingSubsidies(String corporateNumber, String syncId) {
        subsidyMapper.softDeleteMissingCompanySubsidies(corporateNumber, syncId);
    }
}
