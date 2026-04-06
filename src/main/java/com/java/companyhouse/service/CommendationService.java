package com.java.companyhouse.service;

import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CommendationMapper;
import com.java.companyhouse.model.dto.CommendationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

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
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        commendationMapper.softDeleteAllCompanyCommendations(corporateNumber);
    }

    @Override
    protected void upsert(CommendationDto entity, String syncId) {
        commendationMapper.upsertCommendation(entity);
        commendationMapper.upsertCompanyCommendation(entity, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        commendationMapper.softDeleteMissingCompanyCommendations(corporateNumber, syncId);
    }
}