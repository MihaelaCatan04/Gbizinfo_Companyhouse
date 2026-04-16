package com.java.companyhouse.service;

import com.java.companyhouse.cache.CommendationIdResolver;
import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.CommendationMapper;
import com.java.companyhouse.model.dto.CommendationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CommendationService extends AbstractBatchService<CommendationDto> {

    private final CommendationMapper commendationMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final CommendationIdResolver commendationIdResolver;

    public CommendationService(CommendationMapper commendationMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, CommendationIdResolver commendationIdResolver) {
        super(transactionTemplate);
        this.commendationMapper = commendationMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.commendationIdResolver = commendationIdResolver;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        commendationMapper.softDeleteAllCompanyCommendations(companyId);
    }

    @Override
    protected void upsert(CommendationDto entity, String syncId) {
        commendationMapper.upsertCommendation(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long commendationId = commendationIdResolver.resolve(entity.getMergeKey());

        commendationMapper.upsertCompanyCommendation(companyId, commendationId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        commendationMapper.softDeleteMissingCompanyCommendations(companyId, syncId);
    }

    @Override
    protected String getCorporateNumber(CommendationDto entity) {
        return entity.getCorporateNumber();
    }
}