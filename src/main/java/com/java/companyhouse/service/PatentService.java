package com.java.companyhouse.service;

import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.PatentIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.PatentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class PatentService extends AbstractBatchService<PatentDto> {

    private final PatentMapper patentMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final PatentIdResolver patentIdResolver;
    private final ClassificationService classificationService;

    public PatentService(PatentMapper patentMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, PatentIdResolver patentIdResolver, ClassificationService classificationService) {
        super(transactionTemplate);
        this.patentMapper = patentMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.patentIdResolver = patentIdResolver;
        this.classificationService = classificationService;
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void onEmpty(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        patentMapper.softDeleteAllCompanyPatents(companyId);
        classificationService.deleteOrphanedPatentClassifications(corporateNumber);
    }

    @Override
    protected void upsert(PatentDto entity, String syncId) {
        patentMapper.upsertPatent(entity);

        Long companyId = companyIdResolver.resolve(entity.getCorporateNumber());
        Long patentId = patentIdResolver.resolve(entity.getMergeKey());

        patentMapper.upsertCompanyPatent(companyId, patentId, syncId);
    }

    @Override
    protected void cleanup(String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        patentMapper.softDeleteMissingCompanyPatents(companyId, syncId);
        classificationService.deleteOrphanedPatentClassifications(corporateNumber);
    }

    @Override
    protected String getCorporateNumber(PatentDto entity) {
        return entity.getCorporateNumber();
    }
}