package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

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
    protected void processSnapshot(CompanySnapshot<PatentDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<PatentDto> patents = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (patents.isEmpty()) {
            deleteAllPatents(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertPatents(patents, syncId);
        cleanupMissingPatents(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<PatentDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllPatents(String corporateNumber) {
        patentMapper.softDeleteAllCompanyPatents(corporateNumber);
    }

    private void upsertPatents(List<PatentDto> patents, String syncId) {
        for (PatentDto dto : patents) {
            if (dto == null) continue;
            patentMapper.upsertPatent(dto);
            patentMapper.upsertCompanyPatent(dto, syncId);
        }
    }

    private void cleanupMissingPatents(String corporateNumber, String syncId) {
        patentMapper.softDeleteMissingCompanyPatents(corporateNumber, syncId);
    }
}
