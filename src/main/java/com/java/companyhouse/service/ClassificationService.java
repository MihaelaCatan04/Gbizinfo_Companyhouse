package com.java.companyhouse.service;

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

    public ClassificationService(ClassificationMapper classificationMapper, AdvisoryLockMapper advisoryLockMapper, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.classificationMapper = classificationMapper;
        this.advisoryLockMapper = advisoryLockMapper;
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
        classificationMapper.softDeleteAllPatentClassifications(corporateNumber, parentMergeKey);
    }

    @Override
    protected void cleanup(String corporateNumber, String parentMergeKey, String syncId) {
        classificationMapper.softDeleteMissingPatentClassifications(corporateNumber, parentMergeKey, syncId);
    }

    @Override
    protected void upsertAll(List<ClassificationDto> items, String parentMergeKey, String corporateNumber, String syncId) {
        for (ClassificationDto dto : items) {
            classificationMapper.upsertClassification(dto);
            classificationMapper.upsertPatentClassification(dto.getMergeKey(), parentMergeKey, corporateNumber, syncId);
        }
    }

    public void deleteOrphanedPatentClassifications(String corporateNumber) {
        classificationMapper.softDeleteOrphanedPatentClassifications(corporateNumber);
    }
}