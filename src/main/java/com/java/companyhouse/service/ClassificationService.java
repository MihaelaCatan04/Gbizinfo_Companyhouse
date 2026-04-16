package com.java.companyhouse.service;

import com.java.companyhouse.cache.ClassificationIdResolver;
import com.java.companyhouse.cache.CompanyIdResolver;
import com.java.companyhouse.cache.PatentIdResolver;
import com.java.companyhouse.mapper.AdvisoryLockMapper;
import com.java.companyhouse.mapper.ClassificationMapper;
import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.model.receiver.PatentClassificationSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class ClassificationService extends AbstractNestedBatchService<PatentClassificationSnapshot, ClassificationDto> {

    private final ClassificationMapper classificationMapper;
    private final AdvisoryLockMapper advisoryLockMapper;
    private final CompanyIdResolver companyIdResolver;
    private final PatentIdResolver patentIdResolver;
    private final ClassificationIdResolver classificationIdResolver;

    public ClassificationService(ClassificationMapper classificationMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate, CompanyIdResolver companyIdResolver, PatentIdResolver patentIdResolver, ClassificationIdResolver classificationIdResolver) {
        super(transactionTemplate);
        this.classificationMapper = classificationMapper;
        this.advisoryLockMapper = advisoryLockMapper;
        this.companyIdResolver = companyIdResolver;
        this.patentIdResolver = patentIdResolver;
        this.classificationIdResolver = classificationIdResolver;
    }

    @Override
    protected String getMergeKey(ClassificationDto item) {
        return item.getMergeKey();
    }

    @Override
    protected void acquireLock(String corporateNumber) {
        advisoryLockMapper.acquireAdvisoryLock(corporateNumber);
    }

    @Override
    protected void deleteAll(String corporateNumber, String parentMergeKey) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long patentId = patentIdResolver.resolve(parentMergeKey);
        classificationMapper.softDeleteAllPatentClassifications(companyId, patentId);
    }

    @Override
    protected void cleanup(String corporateNumber, String parentMergeKey, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long patentId = patentIdResolver.resolve(parentMergeKey);
        classificationMapper.softDeleteMissingPatentClassifications(companyId, patentId, syncId);
    }

    @Override
    protected void upsertAll(List<ClassificationDto> items, String parentMergeKey, String corporateNumber, String syncId) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        Long patentId = patentIdResolver.resolve(parentMergeKey);

        for (ClassificationDto dto : items) {
            classificationMapper.upsertClassification(dto);
            Long classificationId = classificationIdResolver.resolve(dto.getMergeKey());
            classificationMapper.upsertPatentClassification(companyId, patentId, classificationId, syncId);
        }
    }

    public void deleteOrphanedPatentClassifications(String corporateNumber) {
        Long companyId = companyIdResolver.resolve(corporateNumber);
        classificationMapper.softDeleteOrphanedPatentClassifications(companyId);
    }

    @Override
    protected String getCorporateNumber(ClassificationDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getParentMergeKey(ClassificationDto entity) {
        return entity.getPatentMergeKey();
    }
}