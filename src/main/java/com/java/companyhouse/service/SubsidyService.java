package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.SubsidyIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.SubsidyMapper;
import com.java.companyhouse.model.dto.SubsidyDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class SubsidyService extends AbstractBatchService<SubsidyDto> {

    private final SubsidyMapper subsidyMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final SubsidyIdResolver subsidyIdResolver;

    public SubsidyService(SubsidyMapper subsidyMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, SubsidyIdResolver subsidyIdResolver) {
        super(transactionTemplate);
        this.subsidyMapper = subsidyMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.subsidyIdResolver = subsidyIdResolver;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        subsidyMapper.softDeleteAllCompanySubsidies(companyId);
    }

    @Override
    protected void upsert(SubsidyDto entity, String syncId) {
        subsidyMapper.upsertSubsidy(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long subsidyId = subsidyIdResolver.resolve(entity.getMergeKey());

        subsidyMapper.upsertCompanySubsidy(companyId, subsidyId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        subsidyMapper.softDeleteMissingCompanySubsidies(companyId, syncId);
    }

    @Override
    protected String getCorporateNumber(SubsidyDto entity) {
        return entity.getCorporateNumber();
    }
}