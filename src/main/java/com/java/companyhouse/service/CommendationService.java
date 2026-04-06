package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CommendationMapper;
import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class CommendationService extends AbstractBatchService<CommendationDto> {

    private final CommendationMapper commendationMapper;
    private final AdvisoryLockMapper advisoryLockMapper;

    public CommendationService(CommendationMapper commendationMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.commendationMapper = commendationMapper;
        this.advisoryLockMapper = advisoryLockMapper;
    }

    @Override
    protected void processSnapshot(CompanySnapshot<CommendationDto> snapshot) {
        if (!isValidSnapshot(snapshot)) return;

        String corporateNumber = snapshot.getCorporateNumber();
        List<CommendationDto> commendations = snapshot.getEntities();

        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);

        if (commendations.isEmpty()) {
            deleteAllCommendations(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        upsertCommendations(commendations, syncId);
        cleanupMissingCommendations(corporateNumber, syncId);
    }

    private boolean isValidSnapshot(CompanySnapshot<CommendationDto> snapshot) {
        return snapshot != null && snapshot.getCorporateNumber() != null;
    }

    private void deleteAllCommendations(String corporateNumber) {
        commendationMapper.softDeleteAllCompanyCommendations(corporateNumber);
    }

    private void upsertCommendations(List<CommendationDto> commendations, String syncId) {
        for (CommendationDto dto : commendations) {
            if (dto == null) continue;
            commendationMapper.upsertCommendation(dto);
            commendationMapper.upsertCompanyCommendation(dto, syncId);
        }
    }

    private void cleanupMissingCommendations(String corporateNumber, String syncId) {
        commendationMapper.softDeleteMissingCompanyCommendations(corporateNumber, syncId);
    }
}
